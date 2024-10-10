package org.jeecg.modules.schedule.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.schedule.entity.Schedule;
import org.jeecg.modules.schedule.service.IScheduleService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: 医生排班
 * @Author: jeecg-boot
 * @Date:   2024-09-11
 * @Version: V1.0
 */
@Api(tags="医生排班")
@RestController
@RequestMapping("/schedule/schedule")
@Slf4j
public class ScheduleController extends JeecgController<Schedule, IScheduleService> {
	@Autowired
	private IScheduleService scheduleService;
	
	/**
	 * 分页列表查询
	 *
	 * @param schedule
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "医生排班-分页列表查询")
	@ApiOperation(value="医生排班-分页列表查询", notes="医生排班-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Schedule>> queryPageList(Schedule schedule,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Schedule> queryWrapper = QueryGenerator.initQueryWrapper(schedule, req.getParameterMap());
		Page<Schedule> page = new Page<Schedule>(pageNo, pageSize);
		IPage<Schedule> pageList = scheduleService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param schedule
	 * @return
	 */
	@AutoLog(value = "医生排班-添加")
	@ApiOperation(value="医生排班-添加", notes="医生排班-添加")
	@RequiresPermissions("schedule:schedule:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Schedule schedule) {
		scheduleService.save(schedule);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param schedule
	 * @return
	 */
	@AutoLog(value = "医生排班-编辑")
	@ApiOperation(value="医生排班-编辑", notes="医生排班-编辑")
	@RequiresPermissions("schedule:schedule:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Schedule schedule) {
		scheduleService.updateById(schedule);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "医生排班-通过id删除")
	@ApiOperation(value="医生排班-通过id删除", notes="医生排班-通过id删除")
	@RequiresPermissions("schedule:schedule:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scheduleService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "医生排班-批量删除")
	@ApiOperation(value="医生排班-批量删除", notes="医生排班-批量删除")
	@RequiresPermissions("schedule:schedule:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scheduleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "医生排班-通过id查询")
	@ApiOperation(value="医生排班-通过id查询", notes="医生排班-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Schedule> queryById(@RequestParam(name="id",required=true) String id) {
		Schedule schedule = scheduleService.getById(id);
		if(schedule==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(schedule);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param schedule
    */
    @RequiresPermissions("schedule:schedule:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Schedule schedule) {
        return super.exportXls(request, schedule, Schedule.class, "医生排班");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("schedule:schedule:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Schedule.class);
    }

}
