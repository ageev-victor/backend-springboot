package ru.ageev.tasklist.backendspringboot.controller;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ageev.tasklist.backendspringboot.entity.Category;
import ru.ageev.tasklist.backendspringboot.repo.CategoryRepository;
import ru.ageev.tasklist.backendspringboot.search.CategorySearchValues;

import java.util.List;
import java.util.NoSuchElementException;

// используем @RestController вместо обычного @Controller, чтобы все ответы сразу оборачивались в JSON
// иначе пришлось бы выполнять лишнюю работу, использовать @ResponseBody для ответа, указывать тип отправки JSON
@RestController
@RequestMapping("/category") // базовый адрес
public class CategoryController {

    // доступ к данным из БД
    private final CategoryRepository categoryRepository;

    // автоматическое внедрение экземпляра класса через конструктор
    // не используем @Autowired ля переменной класса, т.к. "Field injection is not recommended "
    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    // для тестирования адрес: http://localhost:8080/category/test
    @GetMapping("/test")
    public List<Category> test() {

        List<Category> list = categoryRepository.findAll();


        return list; // JSON формат будет использоваться автоматически

    }

    @PostMapping("/add")
    public ResponseEntity<Category> add(@RequestBody Category category) {
        //Проверка на обязательные параметры
        if (category.getId() == null || category.getId() != 0) {
            return new ResponseEntity("Redurant param, id must be null", HttpStatus.NOT_ACCEPTABLE);
        }

        //Если передали пустое значение Title
        if (category.getTitle() == null || category.getTitle().trim().length() != 0) {
            return new ResponseEntity("Missed param: Title", HttpStatus.NOT_ACCEPTABLE);
        }

        //Если передали пустое значение Title
        if (category.getCompletedCount() == null || category.getCompletedCount() != 0) {
            return new ResponseEntity("Missed param: Title", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(categoryRepository.save(category));
    }


    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Category category) {

        // проверка на обязательные параметры
        if (category.getId() == null || category.getId() == 0) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        // если передали пустое значение title
        if (category.getTitle() == null || category.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }

        // save работает как на добавление, так и на обновление
        return ResponseEntity.ok(categoryRepository.save(category));

    }


    @GetMapping("/id/{id}")
    public ResponseEntity<Category> findById(@PathVariable Long id) {

        Category category = null;

        try {
            category = categoryRepository.findById(id).get();
        } catch (NoSuchElementException e) { // если объект не будет найден
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(category);
    }


    // параметр id передаются не в BODY запроса, а в самом URL
    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id) {

        // можно обойтись и без try-catch, тогда будет возвращаться полная ошибка (stacktrace)
        // здесь показан пример, как можно обрабатывать исключение и отправлять свой текст/статус
        try {
            categoryRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK); // не возвращаем удаленный объект
    }

    // поиск по любым параметрам CategorySearchValues
    @PostMapping("/search")
    public ResponseEntity<List<Category>> search(@RequestBody CategorySearchValues categorySearchValues) {

        // если вместо текста будет пусто или null - вернутся все категории
        return ResponseEntity.ok(categoryRepository.findByTitle(categorySearchValues.getText()));
    }


}
