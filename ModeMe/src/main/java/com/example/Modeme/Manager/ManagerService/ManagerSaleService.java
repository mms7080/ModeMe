package com.example.Modeme.Manager.ManagerService;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.Modeme.Manager.Entity.AddItem;
import com.example.Modeme.Manager.Entity.ItemColor;
import com.example.Modeme.Manager.Entity.ItemColorName;
import com.example.Modeme.Manager.Entity.ItemSize;
import com.example.Modeme.Manager.ManagerDTO.ProductSaleDTO;
import com.example.Modeme.Manager.ManagerRepository.AddItemRepository;
import com.example.Modeme.Manager.ManagerRepository.ProductImageRepository;
import com.example.Modeme.Manager.ManagerRepository.itemColorNameRepository;
import com.example.Modeme.Manager.ManagerRepository.itemColorRepository;
import com.example.Modeme.Manager.ManagerRepository.itemSizeRepository;
import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.User.UserRepository.UserRepository;
import com.example.Modeme.purchase.dao.PurchaseRepository;
import com.example.Modeme.purchase.dto.Purchase;

import jakarta.transaction.Transactional;

@Service
public class ManagerSaleService {

    @Autowired
    private PurchaseRepository pr;

    @Autowired
    private AddItemRepository ar;

    @Autowired
    private UserRepository ur;
    
    @Autowired
    private ProductImageRepository pir;

    @Autowired
    private itemColorNameRepository icr;
    
    @Autowired
    private itemSizeRepository isr;
    
    // 기본 판매 목록 조회 (내림차순 정렬)
    public Page<ProductSaleDTO> getSaleData(Pageable pageable, String newProcess, String searchOption, String keyword) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.desc("orderDate")));
        Page<Purchase> purchases;
        
        // 검색 옵션에 따라 주문 데이터를 필터링
        if ("process".equals(searchOption) && keyword != null) {
        	// 주문 상태로 검색
            String internalProcessStatus = mapKoreanToProcess(keyword);
            purchases = pr.findByProcess(internalProcessStatus, sortedPageable);
        } else if ("orderInfo".equals(searchOption) && keyword != null) {
        	// 상품 이름으로 검색
            purchases = pr.findByItemnameContaining(keyword, sortedPageable);
        } else if ("orderId".equals(searchOption) && keyword != null) {
        	// 유저 아이디로 검색
            purchases = pr.findByUsernameContaining(keyword, sortedPageable);
        } else {
        	// 모든 주문 데이터를 가져옴
            purchases = pr.findAll(sortedPageable);
        }
        
        // 데이터 변환: DTO로 변환하여 반환
        return purchases.map(purchase -> {
        	 // 주문 상태가 없으면 기본값으로 설정
            if (purchase.getProcess() == null || purchase.getProcess().isEmpty()) {
                purchase.setProcess("before");
                pr.save(purchase);
             // 새로운 상태로 변경
            } else if (newProcess != null && !newProcess.isEmpty()) {
                purchase.setProcess(newProcess);
                pr.save(purchase);
            }

            // ✅ 상품 카테고리 조회
            String category = ar.findById((long) purchase.getProductNumber())
                                .map(AddItem::getCategory)
                                .orElse("상품없음");

            // ✅ 유저 이름 조회
            String name = ur.findById((long) purchase.getUserId())
                            .map(User::getName)
                            .orElse("Unknown User");

            // ✅ 첫 번째 상품 이미지 URL 조회
            String firstImageUrl = pir.findFirstImageByProductId((long) purchase.getProductNumber())
                                      .stream().findFirst().orElse("defaultImageUrl");

            // ✅ 색상명 조회
            String colorName = getColorNameById(purchase.getColorId());

            // ✅ 사이즈명 조회
            String sizeName = getSizeNameById(purchase.getSizeId());

            // ✅ 주문 날짜 변환
            Date formattedOrderDate = Date.valueOf(purchase.getOrderDate().toLocalDate());

            // DTO 생성 후 반환
            return new ProductSaleDTO(
                purchase.getId(),
                formattedOrderDate,
                category,
                purchase.getItemname(),
                purchase.getProductMany(),
                purchase.getTotalPrice(),
                purchase.getUsername(),
                name,
                purchase.getProcess(),
                firstImageUrl,
                colorName,
                sizeName
            );
        });
    }
    
    // 색상 ID로 색상 이름 조회
    private String getColorNameById(String colorId) {
        if (colorId == null || colorId.isEmpty()) {
            System.out.println("ColorId가 null 또는 빈 값입니다.");
            return "미등록 색상";
        }

        try {
            Optional<ItemColorName> colorOpt = icr.findById(Long.parseLong(colorId));
            if (colorOpt.isPresent()) {
                System.out.println("색상 조회 성공: " + colorOpt.get().getColorName());
                return colorOpt.get().getColorName();
            } else {
                System.out.println("색상 조회 실패: " + colorId);
                return "색상 없음";
            }
        } catch (NumberFormatException e) {
            System.out.println("ColorId 변환 실패: " + colorId);
            return "미등록 색상";
        }
    }

    // 사이즈 ID로 사이즈 이름 조회
    private String getSizeNameById(String sizeId) {
        if (sizeId == null || sizeId.isEmpty()) {
            System.out.println("SizeId가 null 또는 빈 값입니다.");
            return "미등록 사이즈";
        }

        try {
            Optional<ItemSize> sizeOpt = isr.findById(Long.parseLong(sizeId));
            if (sizeOpt.isPresent()) {
                System.out.println("사이즈 조회 성공: " + sizeOpt.get().getItemSize());
                return sizeOpt.get().getItemSize();
            } else {
                System.out.println("사이즈 조회 실패: " + sizeId);
                return "사이즈 없음";
            }
        } catch (NumberFormatException e) {
            System.out.println("SizeId 변환 실패: " + sizeId);
            return "미등록 사이즈";
        }
    }






    // 한국어 상태를 내부 프로세스 상태로 매핑하는 메서드
    private String mapKoreanToProcess(String koreanStatus) {
        switch (koreanStatus) {
            case "입금전":
                return "before";  // 입금전 -> before
            case "배송준비중":
                return "ready";   // 배송준비중 -> ready
            case "배송중":
                return "delivery"; // 배송중 -> delivery
            case "배송완료":
                return "done";    // 배송완료 -> done
            default:
                return "UNKNOWN"; // 예외 처리: Unknown 상태
        }
    }
    
    // 주문 상태를 한국어로 매핑하는 메서드
    private String mapProcessToKorean(String process) {
        switch (process) {
            case "before":
                return "입금전";
            case "ready":
                return "배송준비중";
            case "delivery":
                return "배송중";
            case "done":
                return "배송완료";
            default:
                return "미확인"; // Default unknown process
        }
    }

    // 월별 판매 금액 데이터를 반환
    public Map<String, Integer> getSalesByMonth() {
        List<Purchase> purchases = pr.findAll();

        // 월별 판매 금액 계산
        return purchases.stream()
            .collect(Collectors.groupingBy(purchase -> formatDateToMonth(purchase.getOrderDate()),
                                          Collectors.summingInt(Purchase::getTotalPrice)));
    }

    // 카테고리별 판매 금액 데이터를 반환
    public Map<String, Integer> getSalesByCategory() {
        List<Purchase> purchases = pr.findAll();

        // 카테고리별 판매 금액 계산
        return purchases.stream()
            .collect(Collectors.groupingBy(purchase -> getCategory(purchase.getProductNumber()),
                                          Collectors.summingInt(Purchase::getTotalPrice)));
    }

    // 날짜를 "yyyy-MM" 형식으로 변환하는 헬퍼 메서드
    private String formatDateToMonth(LocalDateTime orderDate) {
        return orderDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    // 상품 번호로 카테고리 조회
    private String getCategory(int productNumber) {
        return ar.findById((long) productNumber) 
                 .map(item -> item.getCategory())
                 .orElse("기타");
    }

    // 날짜를 "yyyy-MM-dd" 형식으로 변환하는 헬퍼 메서드
    private String formatDate(LocalDateTime orderDate) {
        if (orderDate == null) {
            return "";
        }
        return orderDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
    // 주문 상태별 개수 반환
    public Map<String, Long> getOrderCountByProcess() {
        List<Purchase> purchases = pr.findAll();

        // process별로 그룹화하여 개수 카운팅 (null 값은 "UNKNOWN"으로 처리)
        return purchases.stream()
            .collect(Collectors.groupingBy(
                purchase -> Optional.ofNullable(purchase.getProcess()).orElse("UNKNOWN"),
                Collectors.counting()
            ));
    }

    // 주문 상태를 업데이트하는 메서드
    @Transactional
    public String updateSaleProcess(Long id, String newProcess) {
        if (newProcess == null || newProcess.isEmpty()) {
            throw new IllegalArgumentException("변경할 주문 상태가 올바르지 않습니다.");
        }

        return pr.findById(id)
            .map(purchase -> {
                if (!newProcess.equals(purchase.getProcess())) { // 상태가 다를 경우에만 변경
                    purchase.setProcess(newProcess);
                    pr.save(purchase);
                    
                    String koreanProcess = mapProcessToKorean(newProcess);
                    return "주문 상태가 '" + koreanProcess + "'(으)로 변경되었습니다.";
                }
                
                String koreanProcess = mapProcessToKorean(newProcess);
                return "이미 '" + koreanProcess + "' 상태입니다.";
            })
            .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));
    }

   
}

