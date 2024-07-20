package eub.smart.core.jasperreport;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

        @PostMapping("/generate")
        public ResponseEntity<byte[]> generateReport(@RequestParam String reportName,
                                                     @RequestParam("image") MultipartFile imageFile) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            String base64Image = Base64.getEncoder().encodeToString(imageFile.getBytes());
            parameters.put("BASE64_IMAGE", base64Image);

            List<?> data = List.of("Проверка языка");

            byte[] report = reportService.generateReport(reportName, parameters, data);

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + reportName + ".pdf");

            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(report);
        } catch (JRException | IOException | JRRuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
