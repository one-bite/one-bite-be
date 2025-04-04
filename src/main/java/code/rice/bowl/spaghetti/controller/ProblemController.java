package code.rice.bowl.spaghetti.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/problem")
public class ProblemController {

    @PostMapping("/{problemId}/submit")
    public ResponseEntity<?> solveProblem() {
        return ResponseEntity.ok("test world");
    }

    @PostMapping("/make")
    public ResponseEntity<?> addProblem() {
        return ResponseEntity.ok("test world");
    }

}
