package com.ihrm.employee.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.utils.BeanMapUtils;
import com.ihrm.common.utils.DownloadUtils;
import com.ihrm.domain.employee.*;
import com.ihrm.domain.employee.response.EmployeeReportResult;
import com.ihrm.employee.service.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/employees")
public class EmployeeController extends BaseController
{
    @Autowired
    private UserCompanyPersonalService userCompanyPersonalService;
    @Autowired
    private UserCompanyJobsService userCompanyJobsService;
    @Autowired
    private ResignationService resignationService;
    @Autowired
    private TransferPositionService transferPositionService;
    @Autowired
    private PositiveService positiveService;
    @Autowired
    private ArchiveService archiveService;

    /**
     * 员工个人信息保存
     */
    @RequestMapping(value = "/{id}/personalInfo",
                    method = RequestMethod.PUT)
    public Result savePersonalInfo(
            @PathVariable(name = "id")
                    String uid,
            @RequestBody
                    Map map) throws Exception {
        UserCompanyPersonal sourceInfo = BeanMapUtils.mapToBean(map, UserCompanyPersonal.class);
        if (sourceInfo == null) {
            sourceInfo = new UserCompanyPersonal();
        }
        sourceInfo.setUserId(uid);
        sourceInfo.setCompanyId(super.companyId);
        userCompanyPersonalService.save(sourceInfo);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 员工个人信息读取
     */
    @RequestMapping(value = "/{id}/personalInfo",
                    method = RequestMethod.GET)
    public Result findPersonalInfo(
            @PathVariable(name = "id")
                    String uid) throws Exception {
        UserCompanyPersonal info = userCompanyPersonalService.findById(uid);
        if (info == null) {
            info = new UserCompanyPersonal();
            info.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS, info);
    }

    /**
     * 员工岗位信息保存
     */
    @RequestMapping(value = "/{id}/jobs",
                    method = RequestMethod.PUT)
    public Result saveJobsInfo(
            @PathVariable(name = "id")
                    String uid,
            @RequestBody
                    UserCompanyJobs sourceInfo) throws Exception {
        //更新员工岗位信息
        if (sourceInfo == null) {
            sourceInfo = new UserCompanyJobs();
            sourceInfo.setUserId(uid);
            sourceInfo.setCompanyId(super.companyId);
        }
        userCompanyJobsService.save(sourceInfo);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 员工岗位信息读取
     */
    @RequestMapping(value = "/{id}/jobs",
                    method = RequestMethod.GET)
    public Result findJobsInfo(
            @PathVariable(name = "id")
                    String uid) throws Exception {
        UserCompanyJobs info = userCompanyJobsService.findById(uid);
        if (info == null) {
            info = new UserCompanyJobs();
            info.setUserId(uid);
            info.setCompanyId(companyId);
        }
        return new Result(ResultCode.SUCCESS, info);
    }

    /**
     * 离职表单保存
     */
    @RequestMapping(value = "/{id}/leave",
                    method = RequestMethod.PUT)
    public Result saveLeave(
            @PathVariable(name = "id")
                    String uid,
            @RequestBody
                    EmployeeResignation resignation) throws Exception {
        resignation.setUserId(uid);
        resignationService.save(resignation);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 离职表单读取
     */
    @RequestMapping(value = "/{id}/leave",
                    method = RequestMethod.GET)
    public Result findLeave(
            @PathVariable(name = "id")
                    String uid) throws Exception {
        EmployeeResignation resignation = resignationService.findById(uid);
        if (resignation == null) {
            resignation = new EmployeeResignation();
            resignation.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS, resignation);
    }

    /**
     * 导入员工
     */
    @RequestMapping(value = "/import",
                    method = RequestMethod.POST)
    public Result importDatas(
            @RequestParam(name = "file")
                    MultipartFile attachment) throws Exception {
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 调岗表单保存
     */
    @RequestMapping(value = "/{id}/transferPosition",
                    method = RequestMethod.PUT)
    public Result saveTransferPosition(
            @PathVariable(name = "id")
                    String uid,
            @RequestBody
                    EmployeeTransferPosition transferPosition) throws Exception {
        transferPosition.setUserId(uid);
        transferPositionService.save(transferPosition);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 调岗表单读取
     */
    @RequestMapping(value = "/{id}/transferPosition",
                    method = RequestMethod.GET)
    public Result findTransferPosition(
            @PathVariable(name = "id")
                    String uid) throws Exception {
        UserCompanyJobs jobsInfo = userCompanyJobsService.findById(uid);
        if (jobsInfo == null) {
            jobsInfo = new UserCompanyJobs();
            jobsInfo.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS, jobsInfo);
    }

    /**
     * 转正表单保存
     */
    @RequestMapping(value = "/{id}/positive",
                    method = RequestMethod.PUT)
    public Result savePositive(
            @PathVariable(name = "id")
                    String uid,
            @RequestBody
                    EmployeePositive positive) throws Exception {
        positiveService.save(positive);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 转正表单读取
     */
    @RequestMapping(value = "/{id}/positive",
                    method = RequestMethod.GET)
    public Result findPositive(
            @PathVariable(name = "id")
                    String uid) throws Exception {
        EmployeePositive positive = positiveService.findById(uid);
        if (positive == null) {
            positive = new EmployeePositive();
            positive.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS, positive);
    }

    /**
     * 历史归档详情列表
     */
    @RequestMapping(value = "/archives/{month}",
                    method = RequestMethod.GET)
    public Result archives(
            @PathVariable(name = "month")
                    String month,
            @RequestParam(name = "type")
                    Integer type) throws Exception {
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 归档更新
     */
    @RequestMapping(value = "/archives/{month}",
                    method = RequestMethod.PUT)
    public Result saveArchives(
            @PathVariable(name = "month")
                    String month) throws Exception {
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 历史归档列表
     */
    @RequestMapping(value = "/archives",
                    method = RequestMethod.GET)
    public Result findArchives(
            @RequestParam(name = "pagesize")
                    Integer pagesize,
            @RequestParam(name = "page")
                    Integer page,
            @RequestParam(name = "year")
                    String year) throws Exception {
        Map map = new HashMap();
        map.put("year", year);
        map.put("companyId", companyId);
        Page<EmployeeArchive> searchPage = archiveService.findSearch(map, page, pagesize);
        PageResult<EmployeeArchive> pr = new PageResult(searchPage.getTotalElements(), searchPage.getContent());
        return new Result(ResultCode.SUCCESS, pr);
    }

    @RequestMapping(value = "/export/{month}",
                    method = RequestMethod.GET)
    public void export(
            @PathVariable(name = "month")
                    String month, HttpServletResponse response) throws Exception {
        List<EmployeeReportResult> list = userCompanyPersonalService.findByReport(companyId, month);

        final XSSFWorkbook wb = new XSSFWorkbook();
        final XSSFSheet sheet = wb.createSheet();

        // 创建表头
        final XSSFRow headerRow = sheet.createRow(0);
        AtomicInteger headersAi = new AtomicInteger(0);
        Arrays
                .asList("编号,姓名,手机,最高学历,国家地区,护照号,籍贯,生日,属相,入职时间,离职类型,离职原因,离职时间".split(","))
                .forEach(cellValue -> headerRow
                        .createCell(headersAi.getAndIncrement())
                        .setCellValue(cellValue));

        // 创建数据
        AtomicInteger datasAi = new AtomicInteger(0);
        list.forEach(employeeReportResult -> {
            final XSSFRow dataRow = sheet.createRow(datasAi.getAndIncrement());
            // 编号,
            dataRow
                    .createCell(0)
                    .setCellValue(employeeReportResult.getUserId());
            // 姓名,
            dataRow
                    .createCell(1)
                    .setCellValue(employeeReportResult.getUsername());
            // 手机,
            dataRow
                    .createCell(2)
                    .setCellValue(employeeReportResult.getMobile());
            // 最高学历,
            dataRow
                    .createCell(3)
                    .setCellValue(employeeReportResult.getTheHighestDegreeOfEducation());
            // 国家地区,
            dataRow
                    .createCell(4)
                    .setCellValue(employeeReportResult.getNationalArea());
            // 护照号,
            dataRow
                    .createCell(5)
                    .setCellValue(employeeReportResult.getPassportNo());
            // 籍贯,
            dataRow
                    .createCell(6)
                    .setCellValue(employeeReportResult.getNativePlace());
            // 生日,
            dataRow
                    .createCell(7)
                    .setCellValue(employeeReportResult.getBirthday());
            // 属相,
            dataRow
                    .createCell(8)
                    .setCellValue(employeeReportResult.getZodiac());
            // 入职时间,
            dataRow
                    .createCell(9)
                    .setCellValue(employeeReportResult.getTimeOfEntry());
            // 离职类型,
            dataRow
                    .createCell(10)
                    .setCellValue(employeeReportResult.getTypeOfTurnover());
            // 离职原因,
            dataRow
                    .createCell(11)
                    .setCellValue(employeeReportResult.getReasonsForLeaving());
            // 离职时间
            dataRow
                    .createCell(12)
                    .setCellValue(employeeReportResult.getResignationTime());
        });

        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        wb.write(stream);
        new DownloadUtils().download(stream, response, month.concat("月人事报表.xlsx"));
    }
}
