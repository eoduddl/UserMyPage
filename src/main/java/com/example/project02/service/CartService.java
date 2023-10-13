package com.example.project02.service;

import com.example.project02.dto.CartResponse;
import com.example.project02.dto.ChangeAmountRequest;
import com.example.project02.dto.ProductResponse;
import com.example.project02.entity.Cart;
import com.example.project02.entity.CartProduct;
import com.example.project02.entity.Product;
import com.example.project02.entity.User;
import com.example.project02.exception.OutOfStockException;
import com.example.project02.dto.CartRequest;
import com.example.project02.repository.CartProductRepository;
import com.example.project02.repository.CartRepository;
import com.example.project02.repository.ProductRepository;
import com.example.project02.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;

    @Transactional
    public void addProduct(Long userId, CartRequest request) {

        User user = userRepository.findById(userId).orElseThrow(() ->
            new RuntimeException("가입되지 않은 정보입니다."));

        Product product = productRepository.findById(request.getProductId()).orElseThrow(() ->
            new RuntimeException("등록되지 않은 제품입니다."));

        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = Cart.createCart(user);
            cartRepository.save(newCart);
            return newCart;
        });

        CartProduct cartProduct = cartProductRepository.findByCartIdAndProductId(cart.getId(), product.getId());

        // 요청 수량 검사 메소드
        validateQuantity(request, product);

        if (cartProduct == null) {
            cartProduct = CartProduct.createCartProduct(cart, product, request.getQuantity());
            cartProductRepository.save(cartProduct);
        } else {
            cartProduct.setCart(cartProduct.getCart());
            cartProduct.setProduct(cartProduct.getProduct());
            cartProduct.setAmount(cartProduct.getAmount());
            cartProduct.addCount(request.getQuantity());
            if (cartProduct.getAmount() > product.getStockQuantity()) {
                throw new OutOfStockException("재고 부족");
            }
            cartProduct.setPrice(product);
//            cartProductRepository.save(cartProduct);
        }

        cart.setTotalCount(cart.getTotalCount() + request.getQuantity());

        Double totalPrice = cartRepository.calculateTotalPriceByCartId(cart.getId());
        cart.setTotalPrice(totalPrice);
    }



    @Transactional
    public void changeProductAmount(Long userId, ChangeAmountRequest request) {

        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() ->
                new RuntimeException("조회 불가"));

        Long cartId = cart.getId();

        Long cartProductId = request.getCartProductId();
        int amount = request.getAmount();

        CartProduct cartProduct = cartProductRepository.findByIdAndCartId(cartProductId, cartId);

        Product product = cartProductRepository.findProduct(cartProduct.getId(), cartId);

        // 요청 에러 검출 메소드
        validateRequest(request, product, cartProduct, amount);

        cartProduct.setAmount(amount);

        double price = product.getPrice();
        cartProduct.setPrice(amount * price);

        int totalAmount = cartRepository.calculateTotalAmountByCartId(cart.getId());
        cart.setTotalCount(totalAmount);

        Double totalPrice = cartRepository.calculateTotalPriceByCartId(cart.getId());
        cart.setTotalPrice(totalPrice);
    }



    public CartResponse getCart(Long userId) {

        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() ->
                new RuntimeException("조회 불가"));
        Long idUser = cart.getUser().getId();
        int totalCount = cart.getTotalCount();
        double totalPrice = cart.getTotalPrice();

        List<ProductResponse> productDtoList = cart.getCartProducts().stream().map(product ->
                        new ProductResponse(product.getId(),
                                       product.getProduct().getName(),
                                       product.getProduct().getPrice(),
                                       product.getPrice(),
                                       product.getAmount(),
                                       product.getProduct().getStockQuantity()))
                                        .collect(Collectors.toList());

        return new CartResponse(idUser, totalCount, totalPrice, productDtoList);


    }

    /**
    * 요청 에러 검출 메소드
    */
    private void validateRequest(ChangeAmountRequest request, Product product, CartProduct cartProduct, int amount) {
        if (product.getStockQuantity() < request.getAmount()) {
            throw new OutOfStockException("재고 부족");
        }

        if (cartProduct == null) {
            throw new RuntimeException("조회되지 않습니다.");
        }

        int findAmount = cartProduct.getAmount();

        if (findAmount == amount) {
            throw new RuntimeException("이미 담겨 있는 수량과 변경하려는 수량이 같습니다");
        }

        if (amount <= 0) {
            throw new RuntimeException("수량을 다시 입력해주세요");
        }
    }

    /**
     * 요청 수량 검사 메소드
     */
    private void validateQuantity(CartRequest request, Product product) {
        if (product.getStockQuantity() < request.getQuantity()) {
            throw new OutOfStockException("재고 부족");
        }



        if(request.getQuantity() <= 0){
            throw new RuntimeException("최소 1개 이상 입력하십시오.");
        }
    }
}



