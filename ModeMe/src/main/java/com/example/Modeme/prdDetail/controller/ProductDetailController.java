package com.example.Modeme.prdDetail.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Modeme.Manager.Entity.AddItem;
import com.example.Modeme.Manager.ManagerDTO.AddItemDTO;
import com.example.Modeme.Manager.ManagerRepository.AddItemRepository;
import com.example.Modeme.QnA.QnARepository.QnaRepository;
import com.example.Modeme.User.UserDTO.Headerlogin;
import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.User.UserRepository.UserRepository;
import com.example.Modeme.User.UserService.UserService;
import com.example.Modeme.prdDetail.entity.ProductReview;
import com.example.Modeme.prdDetail.repository.ProductReviewRepository;
import com.example.Modeme.prdDetail.service.ProductDetailService;
import com.example.Modeme.prdDetail.service.ProductEditService;

import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/productDetail")
public class ProductDetailController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddItemRepository addItemRepository;

    @Autowired
    private ProductReviewRepository reviewRepository;

    @Autowired
    private QnaRepository qnaRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private final ProductDetailService detailService;

    @Autowired
    private final ProductEditService editService;

    @Autowired
    private Headerlogin keep;

    // 생성자
    public ProductDetailController(ProductDetailService detailService, UserService userService, ProductEditService editService, ProductReviewRepository reviewRepository) {
        this.editService = editService;
        this.detailService = detailService;
        this.userService = userService;
        this.reviewRepository = reviewRepository;
    }

    // 로그인 정보 유지
    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        keep.headerlogin(model, principal);

        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }
    }

    // 수정 페이지 렌더링
    @GetMapping("/productEdit/{id}")
    public String editProductPage(@PathVariable Long id, Model model) {
        AddItem product = editService.findById(id); // 상품 데이터 가져오기
        model.addAttribute("product", product);
        return "/productDetail/productEdit"; // 수정 페이지 템플릿 이름
    }

    // 수정 처리
    @PostMapping("/productEdit/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @ModelAttribute AddItemDTO productDTO,
            RedirectAttributes redirectAttributes,
            Principal principal) {

        if (principal == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/signin";
        }

        try {
            // 수정 로직 (서비스에서 처리 위임)
            editService.updateProduct(id, productDTO);
            redirectAttributes.addFlashAttribute("successMessage", "상품이 성공적으로 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            System.out.println("예외 발생: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "상품 수정 중 오류가 발생했습니다: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("알 수 없는 오류 발생: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "알 수 없는 오류로 인해 상품 수정에 실패했습니다.");
        }

        return "redirect:/productDetail/productDetail/" + id;
    }

    // 상품 상세 정보 조회
    @GetMapping("/productDetail/{id}")
    public String getProductDetail(@PathVariable Long id, Model model, Principal principal) {
        // 상품 정보 조회
        AddItem product = addItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + id));

        // 리뷰 데이터 조회
        List<ProductReview> reviews = reviewRepository.findByAddItemId(id);

        // 모델에 데이터 추가
        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);

        if (product.getProductSizes() != null && !product.getProductSizes().isEmpty()) {
            model.addAttribute("productSizes", product.getProductSizes());
        }

        return "/productDetail/productDetail";
    }


    @GetMapping("/{id}/review")
    public String reviewWritePage(@PathVariable Long id, Model model, Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }
        System.out.println("리뷰 작성 페이지 요청: ID = " + id);
        AddItem product = addItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + id));
        model.addAttribute("product", product);
        return "/productDetail/productReviewWrite"; // 리뷰 작성 페이지
    }

    @PostMapping("/{id}/review")
    @Transactional
    public String saveReview(
            @PathVariable Long id,
            @ModelAttribute ProductReview review,
            Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        System.out.println("리뷰 저장 요청 도착: ID = " + id);
        AddItem product = addItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + id));
        User user = userService.findByUsername(principal.getName());

        review.setId(null);
        review.setAddItem(product);
        review.setUsers(user);
        reviewRepository.save(review);

        System.out.println("리뷰 저장 완료");
        // 정확한 상세 페이지로 리다이렉트
        return "redirect:/productDetail/productDetail/" + id;
    }

}
