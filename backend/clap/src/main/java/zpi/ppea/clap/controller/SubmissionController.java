//package zpi.ppea.clap.controller;
//
//import lombok.AllArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import zpi.ppea.clap.mapstruct.dtos.SubmissionDTO;
//import zpi.ppea.clap.service.SubmissionService;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/submission")
//@AllArgsConstructor
//public class SubmissionController {
//
//    private final SubmissionService submissionService;
//
//    @GetMapping
//    public ResponseEntity<List<SubmissionDTO>> getSubmissions() {
//        return ResponseEntity.ok(submissionService.getSubmissions());
//    }
//
//}
