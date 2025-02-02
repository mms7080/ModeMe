package com.example.Modeme.Manager.ManagerService;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.Modeme.Manager.ManagerDTO.ProductSaleDTO;
import com.example.Modeme.Manager.ManagerRepository.AddItemRepository;
import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.User.UserRepository.UserRepository;
import com.example.Modeme.purchase.dao.PurchaseRepository;
import com.example.Modeme.purchase.dto.Purchase;

@Service
public class ManagerSaleService {

    @Autowired
    private PurchaseRepository pr;

    @Autowired
    private AddItemRepository ar;

    @Autowired
    private UserRepository ur;

    public Page<ProductSaleDTO> getSaleData(Pageable pageable, String newProcess) {
        // 페이지네이션을 내림차순으로 정렬
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.desc("orderDate"))); // orderDate로 내림차순 정렬

        // 주문 목록을 페이징 처리하여 가져옴
        Page<Purchase> purchases = pr.findAll(sortedPageable);

        // 각 주문에 대해 상품 카테고리 및 기타 정보를 추가하여 DTO로 변환
        return purchases.map(purchase -> {
            // 1. 주문 상태(process)가 비어 있거나 null이면 "before"로 수정
            if (purchase.getProcess() == null || purchase.getProcess().isEmpty()) {
                purchase.setProcess("before");  // process 값을 "before"로 설정
                pr.save(purchase);  // 수정된 엔티티를 저장
            } else if (newProcess != null && !newProcess.isEmpty()) {
                // 2. process 값이 newProcess로 제공되면 수정
                purchase.setProcess(newProcess);
                pr.save(purchase);  // 수정된 엔티티를 저장
            }

            // 3. 상품 정보를 가져와서 카테고리 정보를 포함한 DTO를 생성
            String category = ar.findById((long) purchase.getProductNumber())  // Long 타입으로 변환
                                    .map(item -> item.getCategory())  // AddItem에서 카테고리 가져오기
                                    .orElse("기타");  // 카테고리가 없을 경우 "기타"로 설정

            // 4. 주문 일시를 형식에 맞게 변환
            String formattedOrderDate = formatDate(purchase.getOrderDate());

            // 5. 유저 ID로 유저 이름을 가져오기
            String name = ur.findById((long) purchase.getUserId())
                            .map(User::getName)  // 유저 이름을 가져옴
                            .orElse("Unknown User");  // 유저가 없으면 기본값 "Unknown User" 설정

            // 6. ProductSaleDTO 생성하여 반환
            return new ProductSaleDTO(
                purchase.getId(),
                Date.valueOf(formattedOrderDate),
                category,
                purchase.getItemname(),
                purchase.getProductMany(),
                purchase.getTotalPrice(),
                purchase.getUsername(),
                name,
                purchase.getProcess()  // 수정된 process 값 사용
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

        // process별로 그룹화하여 개수 카운팅
        return purchases.stream()
            .collect(Collectors.groupingBy(Purchase::getProcess, Collectors.counting()));
    }
    
    
}

