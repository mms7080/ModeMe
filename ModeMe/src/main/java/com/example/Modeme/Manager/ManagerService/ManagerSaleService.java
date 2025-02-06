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

import com.example.Modeme.Manager.ManagerDTO.ProductSaleDTO;
import com.example.Modeme.Manager.ManagerRepository.AddItemRepository;
import com.example.Modeme.Manager.ManagerRepository.ProductImageRepository;
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


    public Page<ProductSaleDTO> getSaleData(Pageable pageable, String newProcess, String searchOption, String keyword) {
        // 페이지네이션을 내림차순으로 정렬
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.desc("orderDate"))); // orderDate로 내림차순 정렬

        // 조건에 맞는 주문 목록을 페이징 처리하여 가져옴
        Page<Purchase> purchases;
        
        // 검색 옵션에 따라 처리
        if ("process".equals(searchOption) && keyword != null) {
            purchases = pr.findByProcess(keyword, sortedPageable);  // 주문 상태로 검색
        } else if ("orderInfo".equals(searchOption) && keyword != null) {
            // 주문정보 (itemname만 기준으로 검색)
            purchases = pr.findByItemnameContaining(keyword, sortedPageable);  // itemname만을 포함하는 검색
        } else {
            purchases = pr.findAll(sortedPageable);  // 기본적으로 모든 주문 가져오기
        }

        // 각 주문에 대해 상품 카테고리 및 기타 정보를 추가하여 DTO로 변환
        return purchases.map(purchase -> {
            // 주문 상태 변경 로직
            if (purchase.getProcess() == null || purchase.getProcess().isEmpty()) {
                purchase.setProcess("before");
                pr.save(purchase);
            } else if (newProcess != null && !newProcess.isEmpty()) {
                purchase.setProcess(newProcess);
                pr.save(purchase);
            }

            // 상품 카테고리 정보 가져오기
            String category = ar.findById((long) purchase.getProductNumber())
                                .map(item -> item.getCategory())
                                .orElse("상품없음");

            // 유저 이름 가져오기
            String name = ur.findById((long) purchase.getUserId())
                            .map(User::getName)
                            .orElse("Unknown User");

            // 첫 번째 상품 이미지 URL 가져오기
            List<String> firstImageUrls = pir.findFirstImageByProductId((long) purchase.getProductNumber());  
            String firstImageUrl = firstImageUrls.isEmpty() ? "defaultImageUrl" : firstImageUrls.get(0); // 첫 번째 이미지 URL 사용

            // 주문 일시 형식 변환
            String formattedOrderDate = formatDate(purchase.getOrderDate());

            // ProductSaleDTO 생성
            return new ProductSaleDTO(
                purchase.getId(),
                Date.valueOf(formattedOrderDate),
                category,
                purchase.getItemname(),
                purchase.getProductMany(),
                purchase.getTotalPrice(),
                purchase.getUsername(),
                name,
                purchase.getProcess(),
                firstImageUrl  // 첫 번째 이미지 URL 추가
            );
        });
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

    // Helper method to format date to "yyyy-MM" format
    private String formatDateToMonth(LocalDateTime orderDate) {
        return orderDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    // Helper method to get category from productNumber
    private String getCategory(int productNumber) {
        return ar.findById((long) productNumber) // convert int to Long
                 .map(item -> item.getCategory())
                 .orElse("기타");
    }

    // Helper method to format the Date to yyyy-MM-dd
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
                    return "주문 상태가 '" + newProcess + "'(으)로 변경되었습니다.";
                }
                return "이미 '" + newProcess + "' 상태입니다.";
            })
            .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));
    }

   
}

