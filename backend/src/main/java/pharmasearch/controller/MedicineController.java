package pharmasearch.controller;

import pharmasearch.model.Medicine;
import pharmasearch.repository.MedicineRepository;
import pharmasearch.service.SearchService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class MedicineController {

    private final SearchService searchService;

    public MedicineController() {

        MedicineRepository repository = new MedicineRepository();

        this.searchService = new SearchService(repository);
    }

    // =========================================
    // Search medicines
    // =========================================

    @GetMapping("/api/search")
    public List<Medicine> search(
            @RequestParam String query) {

        return searchService.search(query);
    }

    // =========================================
    // Get medicine count
    // =========================================

    @GetMapping("/api/count")
    public int getMedicineCount() {

        return searchService.getMedicineCount();
    }
}

