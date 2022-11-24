package guru.springframework.bootstrap;

import guru.springframework.domain.Category;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@Profile({"dev", "prod"})
public class DataLoaderMySQL implements ApplicationListener<ContextRefreshedEvent> {
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final CategoryRepository categoryRepository;


    public DataLoaderMySQL(UnitOfMeasureRepository unitOfMeasureRepository, CategoryRepository categoryRepository) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if (unitOfMeasureRepository.count() == 0){
            saveUoM();
        }

        if (categoryRepository.count() == 0){
            saveCategories();
        }
        log.debug("Successfully save unit of measure or/and categories to MySQL!");
    }

    private void saveCategories() {
        Set<Category> categories = new HashSet<>();

        Category category = new Category();
        category.setDescription("American");
        categories.add(category);

        Category category2 = new Category();
        category2.setDescription("Italian");
        categories.add(category2);

        Category category3 = new Category();
        category3.setDescription("Mexican");
        categories.add(category3);

        Category category4 = new Category();
        category4.setDescription("Fast Food");
        categories.add(category4);

        categoryRepository.saveAll(categories);
    }

    private void saveUoM() {
        Set<UnitOfMeasure> uoms = new HashSet<>();

        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setDescription("Teaspoon");
        uoms.add(uom);

        UnitOfMeasure uom2 = new UnitOfMeasure();
        uom2.setDescription("Tablespoon");
        uoms.add(uom2);

        UnitOfMeasure uom3 = new UnitOfMeasure();
        uom3.setDescription("Cup");
        uoms.add(uom3);

        UnitOfMeasure uom4 = new UnitOfMeasure();
        uom4.setDescription("Pinch");
        uoms.add(uom4);

        UnitOfMeasure uom5 = new UnitOfMeasure();
        uom5.setDescription("Ounce");
        uoms.add(uom5);

        UnitOfMeasure uom6 = new UnitOfMeasure();
        uom6.setDescription("Each");
        uoms.add(uom6);

        UnitOfMeasure uom7 = new UnitOfMeasure();
        uom7.setDescription("Dash");
        uoms.add(uom7);

        UnitOfMeasure uom8 = new UnitOfMeasure();
        uom8.setDescription("Pint");
        uoms.add(uom8);

        unitOfMeasureRepository.saveAll(uoms);
    }
}
