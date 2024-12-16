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
