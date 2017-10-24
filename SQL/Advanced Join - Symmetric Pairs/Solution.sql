SELECT X, Y
FROM Functions AS tmp1
WHERE X != Y AND EXISTS(
    SELECT * FROM Functions WHERE X > Y AND Y = tmp1.X AND X = tmp1.Y
)

UNION

SELECT X, Y
FROM Functions AS tmp1
WHERE X = Y AND (
    (SELECT COUNT(*) FROM Functions WHERE X = tmp1.X AND Y = tmp1.X) > 1
)

ORDER BY X