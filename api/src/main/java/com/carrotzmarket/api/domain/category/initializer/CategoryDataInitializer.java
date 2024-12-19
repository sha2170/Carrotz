package com.carrotzmarket.api.domain.category.initializer;


import com.carrotzmarket.db.category.CategoryEntity;
import com.carrotzmarket.api.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryDataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        if (categoryRepository.findAll().isEmpty()) {
            // 최상위 카테고리
            CategoryEntity townTrade = createCategory("동네거래", null);
            CategoryEntity townStory = createCategory("동네 이야기", null);
            CategoryEntity food = createCategory("동네 먹거리 찾기", null);
            CategoryEntity experts = createCategory("동네 전문가 찾기", null);

            // 동네거래 하위 카테고리
            CategoryEntity usedTrade = createCategory("중고거래", townTrade);
            createCategory("알바", townTrade);
            createCategory("부동산", townTrade);
            createCategory("중고차", townTrade);

            // 중고거래 하위 카테고리
            List.of("인기매물", "디지털기기", "가구/인테리어", "유아동", "여성의류",
                            "여성잡화", "남성패션/잡화", "생활가전", "생활/주방", "스포츠/레저",
                            "취미/게임/음반", "뷰티/미용", "식물", "가공식품", "건강기능식품",
                            "반려동물용품", "티켓/교환권", "도서", "유아도서", "기타 중고물품", "삽니다")
                    .forEach(name -> createCategory(name, usedTrade));

            // 동네 이야기 하위 카테고리
            createCategory("모임", townStory);
            createCategory("동네 스토리", townStory);

            // 동네 먹거리 찾기 하위 카테고리
            createCategory("음식점", food);
            createCategory("카페/간식", food);
            createCategory("농수산물", food);

            // 동네 전문가 찾기 하위 카테고리
            List.of("운동", "취미/클래스", "미용실", "뷰티", "이사/용달", "청소",
                            "시공", "수리", "학원/과외")
                    .forEach(name -> createCategory(name, experts));

            System.out.println("카테고리가 초기화되었습니다.");
        }
    }

    private CategoryEntity createCategory(String name, CategoryEntity parent) {
        CategoryEntity category = new CategoryEntity();
        category.setName(name);
        category.setParent(parent);
        return categoryRepository.save(category); // 저장된 CategoryEntity 반환
    }
}
