package SubProject.EShop.service;

import SubProject.EShop.domain.Cart;
import SubProject.EShop.domain.CartItem;
import SubProject.EShop.domain.Product;
import SubProject.EShop.domain.User;
import SubProject.EShop.dto.CartItemRequestDto;
import SubProject.EShop.repository.CartItemRepository;
import SubProject.EShop.repository.CartRepository;
import SubProject.EShop.repository.ProductRepository;
import SubProject.EShop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void addProductToCart(Long userId, CartItemRequestDto requestDto){
        //사용자 & 상품 엔티티 조회
        User user=  userRepository.findById(userId)
                .orElseThrow( () -> new IllegalArgumentException("해당 사용자가 없습니다."));
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다."));

        // 사용자의 장바구니 조회 또는 생성
        Cart cart = getOrCreateCart(user);

        // 장바구니에 이미 해당 상품이 있는지 확인
        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(requestDto.getProductId()))
                .findFirst();

        if(existingCartItem.isPresent()){
            // 상품이 이미 있으면 수량 추가 (setter를 통해 변경하면 더티 체킹으로 업데이트)
            CartItem cartItem = existingCartItem.get();
            // 참고: CartItem에 수량을 변경하는 메소드(예: addQuantity)를 만드는 것이 객체지향적으로 더 좋은 설계입니다.
            // 여기서는 편의상 로직을 서비스에 직접 구현합니다.
            cartItem.updateQuantity(cartItem.getQuantity() + requestDto.getQuantity());
        }else{
            // 상품이 없으면 새로 추가
            CartItem newCartItem = new CartItem(cart, product, requestDto.getQuantity());
            cart.getCartItems().add(newCartItem);
            cartItemRepository.save(newCartItem);
        }
    }

    private Cart getOrCreateCart(User user){
        return cartRepository.findByUserId(user.getId())
                .orElseGet( () -> {
                    Cart newCart = new Cart(user);
                    return cartRepository.save(newCart);
                });
    }
}
