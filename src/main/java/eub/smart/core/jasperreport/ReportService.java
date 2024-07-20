package eub.smart.core.jasperreport;

import jakarta.annotation.PostConstruct;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {


    public byte[] generateReport(String reportName, Map<String, Object> parameters, List<?> data) throws JRException, FileNotFoundException {
        // Компиляция JRXML файла
        InputStream reportStream = getClass().getResourceAsStream("/reports/" + reportName + ".jrxml");
        if (reportStream == null) {
            throw new FileNotFoundException("Report file not found: " + "/reports/" + reportName + ".jrxml");
        }
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

//        JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reports/" + reportName + ".jrxml"));

        // Создание источника данных
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

        // Заполнение отчета
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Экспорт отчета в PDF
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
