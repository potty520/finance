# MyBatis XML Mapper Missing Bindings - Investigation Report
## Finance App Backend - 11 Failing Endpoints (500 Errors)

Generated: 2026-06-24
Project: E:\项目\finance\finance-app

---

## EXECUTIVE SUMMARY

11 endpoints return 500 errors due to missing MyBatis XML mapper statement bindings.
The Java mapper interfaces declare custom methods, but NO corresponding XML files exist
in `src/main/resources/mapper/`. MyBatis-Plus can only auto-handle BaseMapper methods
(selectById, selectList, insert, updateById, deleteById, selectPage, selectCount).
Any custom method declared in a Mapper interface REQUIRES an XML file with the SQL.

Configuration note: `mybatis-plus.mapper-locations: classpath*:/mapper/**/*.xml`
Logic delete: `deleted = 0` means active, `deleted = 1` means deleted. ALL custom SQL
must include `AND deleted = 0` (or `WHERE deleted = 0`) to be consistent.

---

## DETAILED FINDINGS PER ENDPOINT

### 1. GET /api/stock/summary → StockIoMapper.selectStockSummary()

CALL CHAIN:
  StockController.summary()
    → stockService.stockSummary()  [StockServiceImpl line 104]
    → ioMapper.selectStockSummary()

MAPPER INTERFACE (StockIoMapper.java):
  List<Map<String, Object>> selectStockSummary();

TABLE: inv_transaction (entity: StockIo)
FIELDS: id, bill_no, io_type, io_date, goods_id, goods_code, goods_name, spec, unit,
        quantity, price, amount, total_cost, warehouse_id, warehouse_name, batch_no,
        business_no, source_module, dept_id, dept_name, remark, status, creator,
        creator_name, create_time, deleted

MISSING XML FILE: src/main/resources/mapper/StockIoMapper.xml

REQUIRED XML:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.finance.module.stock.mapper.StockIoMapper">

    <select id="selectStockSummary" resultType="map">
        SELECT
            goods_id,
            goods_code,
            goods_name,
            spec,
            unit,
            SUM(CASE WHEN io_type = '1' THEN quantity ELSE 0 END) AS total_in_qty,
            SUM(CASE WHEN io_type = '1' THEN COALESCE(total_cost, amount) ELSE 0 END) AS total_in_amount,
            SUM(CASE WHEN io_type IN ('2','3') THEN quantity ELSE 0 END) AS total_out_qty,
            SUM(CASE WHEN io_type IN ('2','3') THEN COALESCE(total_cost, amount) ELSE 0 END) AS total_out_amount,
            COUNT(*) AS transaction_count
        FROM inv_transaction
        WHERE deleted = 0
        GROUP BY goods_id, goods_code, goods_name, spec, unit
        ORDER BY goods_code
    </select>

</mapper>
```

---

### 2. GET /api/stock/balance/list → StockBalanceMapper.selectByGoods()

CALL CHAIN:
  StockController.balanceList(goodsId)
    → balanceMapper.selectByGoods(goodsId)  [direct call, line 107]

MAPPER INTERFACE (StockBalanceMapper.java):
  List<StockBalance> selectByGoods(@Param("goodsId") Long goodsId);

TABLE: inv_balance (entity: StockBalance)
FIELDS: id, goods_id, goods_code, goods_name, spec, unit, warehouse_id,
        warehouse_name, batch_no, begin_quantity, in_quantity, out_quantity,
        end_quantity, begin_amount, in_amount, out_amount, end_amount,
        avg_price, update_time, deleted

MISSING XML FILE: src/main/resources/mapper/StockBalanceMapper.xml

REQUIRED XML:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.finance.module.stock.mapper.StockBalanceMapper">

    <select id="selectByGoods" resultType="com.finance.module.stock.entity.StockBalance">
        SELECT * FROM inv_balance
        WHERE deleted = 0
        <if test="goodsId != null">
            AND goods_id = #{goodsId}
        </if>
        ORDER BY goods_code, warehouse_id
    </select>

    <select id="getEndQuantity" resultType="java.math.BigDecimal">
        SELECT end_quantity FROM inv_balance
        WHERE goods_id = #{goodsId} AND warehouse_id = #{warehouseId} AND deleted = 0
        LIMIT 1
    </select>

    <select id="sumByCategory" resultType="map">
        SELECT
            goods_code,
            goods_name,
            SUM(end_quantity) AS total_quantity,
            SUM(end_amount) AS total_amount
        FROM inv_balance
        WHERE deleted = 0
        GROUP BY goods_code, goods_name
        ORDER BY goods_code
    </select>

</mapper>
```

---

### 3. GET /api/contract/stats → ContractMapper.stats()  **[CONFIRMED ERROR]**

CALL CHAIN:
  ContractController.stats()
    → contractMapper.stats()  [direct call, line 61]

MAPPER INTERFACE (ContractMapper.java):
  List<Map<String, Object>> stats();
  BigDecimal sumAmount(@Param("status") String status);

TABLE: ctr_sales_contract (entity: Contract)
FIELDS: id, contract_no, contract_name, contract_type, party_id, party_name,
        party_contact, party_phone, amount, sign_date, start_date, end_date,
        pay_type, content, attachment, owner_id, owner_name, dept_id, dept_name,
        status, flow_no, remark, create_time, update_time, deleted
STATUS VALUES: 0=待审, 1=生效中, 2=已终止, 3=已完成

MISSING XML FILE: src/main/resources/mapper/ContractMapper.xml

REQUIRED XML:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.finance.module.contract.mapper.ContractMapper">

    <select id="stats" resultType="map">
        SELECT
            status,
            COUNT(*) AS count,
            SUM(amount) AS total_amount
        FROM ctr_sales_contract
        WHERE deleted = 0
        GROUP BY status
        ORDER BY status
    </select>

    <select id="sumAmount" resultType="java.math.BigDecimal">
        SELECT COALESCE(SUM(amount), 0)
        FROM ctr_sales_contract
        WHERE deleted = 0
        <if test="status != null and status != ''">
            AND status = #{status}
        </if>
    </select>

</mapper>
```

---

### 4. GET /api/project/stats → ProjectMapper.stats()

CALL CHAIN:
  ProjectController.stats(status)
    → projectMapper.stats(status)  [direct call, line 54]

MAPPER INTERFACE (ProjectMapper.java):
  List<Map<String, Object>> stats(@Param("status") String status);

TABLE: prj_project (entity: Project)
FIELDS: id, project_code, project_name, project_type, contract_id, contract_no,
        manager_id, manager_name, dept_id, dept_name, budget_amount, used_amount,
        start_date, end_date, status, content, remark, create_time, update_time, deleted
STATUS VALUES: 0=筹备, 1=进行中, 2=暂停, 3=完成, 4=关闭

MISSING XML FILE: src/main/resources/mapper/ProjectMapper.xml

REQUIRED XML:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.finance.module.project.mapper.ProjectMapper">

    <select id="stats" resultType="map">
        SELECT
            status,
            COUNT(*) AS count,
            SUM(budget_amount) AS total_budget,
            SUM(used_amount) AS total_used
        FROM prj_project
        WHERE deleted = 0
        <if test="status != null and status != ''">
            AND status = #{status}
        </if>
        GROUP BY status
        ORDER BY status
    </select>

</mapper>
```

---

### 5. GET /api/report/custom/list → (uses BaseMapper.selectList)

CALL CHAIN:
  ReportController.list()
    → reportService.listCustom()  [ReportServiceImpl line 63]
    → reportMapper.selectList(null)

MAPPER INTERFACE (FinReportMapper.java):
  (No custom methods - only inherits BaseMapper<FinReport>)

TABLE: rpt_template (entity: FinReport)
FIELDS: id, report_code, report_name, report_type, fiscal_year, fiscal_period,
        template_id, content, status, creator, creator_name, create_time,
        update_time, deleted

DIAGNOSIS: This endpoint calls `selectList(null)` which is a BaseMapper method.
MyBatis-Plus handles this automatically — NO XML file is needed.
The 500 error is likely caused by:
  (a) The `rpt_template` table does not exist in the database, OR
  (b) A column name mismatch (check create_time vs createTime mapping)

RECOMMENDATION: Check the database for the `rpt_template` table existence:
  SHOW TABLES LIKE 'rpt_template';
  DESC rpt_template;

---

### 6. GET /api/asset/depreciation/list → FaDepreciationMapper.selectByPeriod()

CALL CHAIN:
  AssetController.listDepreciations(fiscalYear, fiscalPeriod)
    → faService.listDepreciations(year, period)  [FaServiceImpl line 172]
    → depMapper.selectByPeriod(year, period)

MAPPER INTERFACE (FaDepreciationMapper.java):
  List<FaDepreciation> selectByPeriod(@Param("year") String year, @Param("period") Integer period);

TABLE: fa_depreciation (entity: FaDepreciation)
FIELDS: id, card_id, card_code, card_name, fiscal_year, fiscal_period,
        depreciation_date, original_value, period_depreciation,
        accumulated_depreciation, net_value, status, operator,
        operator_name, create_time

NOTE: This entity does NOT have a `deleted` field, so no logic-delete filter needed.

MISSING XML FILE: src/main/resources/mapper/FaDepreciationMapper.xml

REQUIRED XML:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.finance.module.asset.mapper.FaDepreciationMapper">

    <select id="selectByPeriod" resultType="com.finance.module.asset.entity.FaDepreciation">
        SELECT * FROM fa_depreciation
        WHERE fiscal_year = #{year} AND fiscal_period = #{period}
        ORDER BY card_code
    </select>

</mapper>
```

---

### 7. GET /api/cost/allocation/summary → CostAllocationMapper.sumByCenter()

CALL CHAIN:
  CostController.allocationSummary(fiscalYear, fiscalPeriod)
    → allocationMapper.sumByCenter(fiscalYear, fiscalPeriod)  [direct call, line 100]

MAPPER INTERFACE (CostAllocationMapper.java):
  List<Map<String, Object>> sumByCenter(@Param("year") String year, @Param("period") Integer period);

TABLE: cost_allocation (entity: CostAllocation)
FIELDS: id, bill_no, fiscal_year, fiscal_period, allocation_date, allocation_type,
        source_center_code, source_center_name, target_center_code, target_center_name,
        item_code, item_name, amount, source_no, remark, status, operator,
        operator_name, create_time, deleted

MISSING XML FILE: src/main/resources/mapper/CostAllocationMapper.xml

REQUIRED XML:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.finance.module.cost.mapper.CostAllocationMapper">

    <select id="sumByCenter" resultType="map">
        SELECT
            source_center_code,
            source_center_name,
            target_center_code,
            target_center_name,
            SUM(amount) AS total_amount,
            COUNT(*) AS alloc_count
        FROM cost_allocation
        WHERE deleted = 0
          AND fiscal_year = #{year}
          AND fiscal_period = #{period}
        GROUP BY source_center_code, source_center_name,
                 target_center_code, target_center_name
        ORDER BY source_center_code
    </select>

</mapper>
```

---

### 8. GET /api/cost/product/summary → CostProductMapper.summary()

CALL CHAIN:
  CostController.productSummary(fiscalYear, fiscalPeriod)
    → costService.productSummary(year, period)  [CostServiceImpl line 68]
    → productMapper.summary(year, period)

MAPPER INTERFACE (CostProductMapper.java):
  List<Map<String, Object>> summary(@Param("year") String year, @Param("period") Integer period);

TABLE: cost_calculation (entity: CostProduct)
FIELDS: id, fiscal_year, fiscal_period, product_code, product_name, output_quantity,
        direct_material, direct_labor, direct_expense, indirect_material,
        indirect_labor, indirect_expense, total_cost, unit_cost, status,
        create_time, deleted

MISSING XML FILE: src/main/resources/mapper/CostProductMapper.xml

REQUIRED XML:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.finance.module.cost.mapper.CostProductMapper">

    <select id="summary" resultType="map">
        SELECT
            product_code,
            product_name,
            output_quantity,
            direct_material,
            direct_labor,
            direct_expense,
            indirect_material,
            indirect_labor,
            indirect_expense,
            total_cost,
            unit_cost,
            status
        FROM cost_calculation
        WHERE deleted = 0
          AND fiscal_year = #{year}
          AND fiscal_period = #{period}
        ORDER BY product_code
    </select>

</mapper>
```

---

### 9. GET /api/budget/analysis → BudgetMapper.analysis()

CALL CHAIN:
  BudgetController.analysis(fiscalYear, fiscalPeriod)
    → budgetService.analysis(year, period)  [BudgetServiceImpl line 109]
    → budgetMapper.analysis(year, period)

MAPPER INTERFACE (BudgetMapper.java):
  List<Map<String, Object>> analysis(@Param("year") String year, @Param("period") Integer period);
  BigDecimal sumAmount(@Param("year") String year, @Param("period") Integer period);
  Budget findBudget(@Param("year") String year, @Param("period") Integer period,
                    @Param("subjectCode") String subjectCode, @Param("deptId") Long deptId);

TABLE: budget_master (entity: Budget)
FIELDS: id, budget_no, fiscal_year, fiscal_period, budget_type, subject_code,
        subject_name, dept_id, dept_name, project_id, project_name, amount,
        used_amount, available_amount, status, remark, creator, creator_name,
        create_time, update_time, deleted

MISSING XML FILE: src/main/resources/mapper/BudgetMapper.xml

REQUIRED XML:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.finance.module.budget.mapper.BudgetMapper">

    <select id="analysis" resultType="map">
        SELECT
            subject_code,
            subject_name,
            dept_name,
            budget_type,
            SUM(amount) AS total_budget,
            SUM(used_amount) AS total_used,
            SUM(available_amount) AS total_available
        FROM budget_master
        WHERE deleted = 0
          AND fiscal_year = #{year}
          AND fiscal_period = #{period}
        GROUP BY subject_code, subject_name, dept_name, budget_type
        ORDER BY subject_code
    </select>

    <select id="sumAmount" resultType="java.math.BigDecimal">
        SELECT COALESCE(SUM(amount), 0)
        FROM budget_master
        WHERE deleted = 0
          AND fiscal_year = #{year}
          AND fiscal_period = #{period}
    </select>

    <select id="findBudget" resultType="com.finance.module.budget.entity.Budget">
        SELECT * FROM budget_master
        WHERE deleted = 0
          AND fiscal_year = #{year}
          AND fiscal_period = #{period}
          AND subject_code = #{subjectCode}
          AND dept_id = #{deptId}
        LIMIT 1
    </select>

</mapper>
```

---

### 10. GET /api/expense/apply/analysis → ExpenseApplyMapper.sumBySubject()

CALL CHAIN:
  ExpenseController.analysis(fiscalYear, fiscalPeriod)
    → expenseService.analysis(year, period)  [ExpenseServiceImpl line 57]
    → applyMapper.sumBySubject(year, period)

MAPPER INTERFACE (ExpenseApplyMapper.java):
  List<Map<String, Object>> sumBySubject(@Param("year") String year, @Param("period") Integer period);

TABLE: exp_application (entity: ExpenseApply)
FIELDS: id, bill_no, apply_type, subject_code, subject_name, amount, apply_date,
        applicant, applicant_name, dept_id, dept_name, reason, attachment,
        status, flow_no, remark, create_time, update_time, deleted

NOTE: The entity has `applyDate` (LocalDate) but no explicit `fiscalYear`/`fiscalPeriod`.
The query must derive the fiscal period from apply_date (year and month).

MISSING XML FILE: src/main/resources/mapper/ExpenseApplyMapper.xml

REQUIRED XML:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.finance.module.expense.mapper.ExpenseApplyMapper">

    <select id="sumBySubject" resultType="map">
        SELECT
            subject_code,
            subject_name,
            COUNT(*) AS apply_count,
            SUM(amount) AS total_amount
        FROM exp_application
        WHERE deleted = 0
          AND YEAR(apply_date) = #{year}
          AND MONTH(apply_date) = #{period}
        GROUP BY subject_code, subject_name
        ORDER BY total_amount DESC
    </select>

</mapper>
```

---

### 11. GET /api/consol/analysis → ConsolOffsetMapper.analysis()

CALL CHAIN:
  ConsolController.analysis(fiscalYear, fiscalPeriod)
    → consolService.analysis(year, period)  [ConsolServiceImpl line 33]
    → offsetMapper.analysis(year, period)

MAPPER INTERFACE (ConsolOffsetMapper.java):
  List<Map<String, Object>> analysis(@Param("year") String year, @Param("period") Integer period);
  BigDecimal sumByType(@Param("year") String year, @Param("period") Integer period);

TABLE: con_offset (entity: ConsolOffset)
FIELDS: id, fiscal_year, fiscal_period, offset_type, subject_code, subject_name,
        summary, amount, source_company, target_company, remark, status,
        operator, operator_name, create_time, deleted

MISSING XML FILE: src/main/resources/mapper/ConsolOffsetMapper.xml

REQUIRED XML:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.finance.module.consol.mapper.ConsolOffsetMapper">

    <select id="analysis" resultType="map">
        SELECT
            offset_type,
            subject_code,
            subject_name,
            source_company,
            target_company,
            SUM(amount) AS total_amount,
            COUNT(*) AS record_count
        FROM con_offset
        WHERE deleted = 0
          AND fiscal_year = #{year}
          AND fiscal_period = #{period}
        GROUP BY offset_type, subject_code, subject_name,
                 source_company, target_company
        ORDER BY offset_type, subject_code
    </select>

    <select id="sumByType" resultType="java.math.BigDecimal">
        SELECT COALESCE(SUM(amount), 0)
        FROM con_offset
        WHERE deleted = 0
          AND fiscal_year = #{year}
          AND fiscal_period = #{period}
    </select>

</mapper>
```

---

## SUMMARY TABLE

| # | Endpoint                              | Mapper Interface           | Method              | XML Needed? | Table              |
|---|---------------------------------------|----------------------------|---------------------|-------------|--------------------|
| 1 | GET /api/stock/summary                | StockIoMapper              | selectStockSummary  | YES         | inv_transaction    |
| 2 | GET /api/stock/balance/list           | StockBalanceMapper         | selectByGoods       | YES         | inv_balance        |
| 3 | GET /api/contract/stats               | ContractMapper             | stats               | YES         | ctr_sales_contract |
| 4 | GET /api/project/stats                | ProjectMapper              | stats               | YES         | prj_project        |
| 5 | GET /api/report/custom/list           | FinReportMapper            | selectList (Base)   | NO *        | rpt_template       |
| 6 | GET /api/asset/depreciation/list      | FaDepreciationMapper       | selectByPeriod      | YES         | fa_depreciation    |
| 7 | GET /api/cost/allocation/summary      | CostAllocationMapper       | sumByCenter         | YES         | cost_allocation    |
| 8 | GET /api/cost/product/summary         | CostProductMapper          | summary             | YES         | cost_calculation   |
| 9 | GET /api/budget/analysis              | BudgetMapper               | analysis            | YES         | budget_master      |
|10 | GET /api/expense/apply/analysis       | ExpenseApplyMapper         | sumBySubject        | YES         | exp_application    |
|11 | GET /api/consol/analysis              | ConsolOffsetMapper         | analysis            | YES         | con_offset         |

* Endpoint 5 uses BaseMapper.selectList(null) — handled by MyBatis-Plus automatically.
  Error is likely a missing table or column issue, not a missing XML binding.

## FILES THAT NEED TO BE CREATED

1. src/main/resources/mapper/StockIoMapper.xml
2. src/main/resources/mapper/StockBalanceMapper.xml
3. src/main/resources/mapper/ContractMapper.xml
4. src/main/resources/mapper/ProjectMapper.xml
5. src/main/resources/mapper/FaDepreciationMapper.xml
6. src/main/resources/mapper/CostAllocationMapper.xml
7. src/main/resources/mapper/CostProductMapper.xml
8. src/main/resources/mapper/BudgetMapper.xml
9. src/main/resources/mapper/ExpenseApplyMapper.xml
10. src/main/resources/mapper/ConsolOffsetMapper.xml

ADDITIONAL METHODS INCLUDED IN XML (also missing bindings but not directly
causing the 11 reported endpoints):
- StockBalanceMapper.getEndQuantity
- StockBalanceMapper.sumByCategory
- ContractMapper.sumAmount
- BudgetMapper.sumAmount
- BudgetMapper.findBudget
- ConsolOffsetMapper.sumByType
