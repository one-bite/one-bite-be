package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.entity.Category;
import code.rice.bowl.spaghetti.exception.InvalidRequestException;
import code.rice.bowl.spaghetti.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 전체 카테고리 조회
     */
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    /**
     * 카테고리 한 개 조회
     */
    public Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("Category not found: " + id));
    }


    // MVP 단계에서 아래의 서비스 레이어는 사용하지 않을 예정

    /**
     * 카테고리 생성
     */
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    /**
     * 카테고리 수정
     */
    public Category update(Long id, Category updated) {
        Category existing = getCategory(id);
        existing.setCategoryName(updated.getCategoryName());
        existing.setDescription(updated.getDescription());
        return categoryRepository.save(existing);
    }

    /**
     * 카테고리 삭제
     */
    public void delete(Long id) {
        Category existing = getCategory(id);
        categoryRepository.delete(existing);
    }
}
