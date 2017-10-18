SELECT
company_code,
founder,
(SELECT COUNT(DISTINCT lead_manager_code) FROM Lead_Manager AS tmp1 WHERE tmp1.company_code = Company.company_code) AS LM_cnt,
(SELECT COUNT(DISTINCT senior_manager_code) FROM Senior_Manager AS tmp2 WHERE tmp2.company_code = Company.company_code) AS SM_cnt,
(SELECT COUNT(DISTINCT manager_code) FROM Manager AS tmp3 WHERE tmp3.company_code = Company.company_code) AS M_cnt,
(SELECT COUNT(DISTINCT employee_code) FROM Employee AS tmp4 WHERE tmp4.company_code = Company.company_code) AS E_cnt
FROM
Company
GROUP BY company_code, founder
ORDER BY company_code