package com.example.category_service.test;

import com.example.category_service.dto.CategoryDto;
import com.example.category_service.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
"동네거리 > 중고거래, 알바, 부동산, 중고차
                  중고거래 > 인기매물, 디지털기기, 가구/인테리어, 유아동, 여성의류, 여성잡화, 남성패션/잡화, 생활가전, 생활/주방, 스포츠/레저, 취미/게임/음반, 뷰티/미용, 식물, 가공식품, 건강기능식품, 반려동물용품, 디켓/교환권, 도서, 유아도서, 기타 중고물품, 삽니다
동네 이야기 > 모임, 동네 스토리
동네 먹거리 찾기 > 음식점, 카페/간식
동네 전문가 찾기 > 운동, 취미/클래스, 미용실, 뷰티, 이사/용달, 청소, 시공, 수리, 학원/과외"
 */

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    void testGetAllCategories() throws Exception {
        // Mock 카테고리 계층 데이터
        CategoryDto rootCategory = new CategoryDto(1L, "동네거리", "root category", true);
        List<CategoryDto> subCategories = new ArrayList<>();
        subCategories.add(new CategoryDto(2L, "중고거래", "sub category", true));
        subCategories.add(new CategoryDto(3L, "알바", "sub category", true));
        subCategories.add(new CategoryDto(4L, "부동산", "sub category", true));
        subCategories.add(new CategoryDto(5L, "중고차", "sub category", true));

        // 하위 카테고리 추가
        CategoryDto secondLevelCategory = new CategoryDto(6L, "디지털기기", "leaf category", true);
        subCategories.get(0).setChildren(List.of(secondLevelCategory)); // "중고거래" 아래에 "디지털기기" 추가
        rootCategory.setChildren(subCategories);

        // Mock Service 응답 설정
        Mockito.when(categoryService.getAllCategories()).thenReturn(List.of(rootCategory));

        // 테스트 요청 및 검증
        mockMvc.perform(get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("동네거리")) // 최상위 카테고리 확인
                .andExpect(jsonPath("$[0].children[0].name").value("중고거래")) // 하위 카테고리 확인
                .andExpect(jsonPath("$[0].children[0].children[0].name").value("디지털기기")); // 2단계 하위 카테고리 확인
    }
}
