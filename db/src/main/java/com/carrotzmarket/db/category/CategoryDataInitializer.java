package com.carrotzmarket.db.category;

import com.carrotzmarket.api.domain.category.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CategoryDataInitializer {

    @Bean
    CommandLineRunner initializeCategories(CategoryRepository categoryRepository) {
        return args -> {
            if (categoryRepository.count() == 0) { // 중복 삽입 방지
                System.out.println("Initializing category data...");

                List<CategoryEntity> categories = new ArrayList<>();

                // 최상위 카테고리
                CategoryEntity neighborhoodTrade = createCategory("동네거래", null);
                CategoryEntity neighborhoodStory = createCategory("동네 이야기", null);
                CategoryEntity localFood = createCategory("동네 먹거리 찾기", null);
                CategoryEntity localExpert = createCategory("동네 전문가 찾기", null);

                // 동네거래 하위 카테고리
                CategoryEntity usedGoods = createCategory("중고거래", neighborhoodTrade);
                addSubcategories(usedGoods, List.of(
                        "인기매물", "디지털기기", "가구/인테리어", "유아동", "여성의류", "여성잡화", 
                        "남성패션/잡화", "생활가전", "생활/주방", "스포츠/레저", "취미/게임/음반", 
                        "뷰티/미용", "식물", "가공식품", "건강기능식품", "반려동물용품", "티켓/교환권", 
                        "도서", "유아도서", "기타 중고물품", "삽니다"
                ));

                // 추가 카테고리 설정
                addSubcategories(neighborhoodStory, List.of("모임", "동네 스토리"));
                addSubcategories(localFood, List.of("음식점", "카페/간식", "농수산물"));
                addSubcategories(localExpert, List.of(
                        "운동", "취미/클래스", "미용실", "뷰티", "이사/용달", "청소", 
                        "시공", "수리", "학원/과외"
                ));

                // 최상위 카테고리 추가
                categories.addAll(List.of(neighborhoodTrade, neighborhoodStory, localFood, localExpert));

                // 데이터베이스 저장
                categoryRepository.saveAll(categories);
                System.out.println("Category data initialized successfully.");
            } else {
                System.out.println("Category data already exists.");
            }
        };
    }

    private CategoryEntity createCategory(String name, CategoryEntity parent) {
        CategoryEntity category = new CategoryEntity();
        category.setName(name);
        category.setParent(parent);
        category.setEnabled(true);

        if (parent != null) {
            parent.getChildren().add(category);
        }
        return category;
    }

    private void addSubcategories(CategoryEntity parent, List<String> subcategoryNames) {
        subcategoryNames.forEach(name -> parent.getChildren().add(createCategory(name, parent)));
    }
}
