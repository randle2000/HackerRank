SELECT
GROUP_CONCAT(FinalResults.OriginalNumber SEPARATOR '&')
FROM 
(
	SELECT
		DivResults.OriginalNumber, 
		COUNT(*)
	FROM 
	(
		SELECT 
			Sequence.x AS OriginalNumber, 
			Sequence.x % NumberGenerator2.x AS DivMod
		FROM 
		(
			SELECT
				NumberGenerator.x
			FROM
			(
				-- Since MySQL doesn't have a number/sequence generator like other DBMS's, 
				-- I'm using this one that @RolandoMySQLDBA answered in this question:
				-- http://dba.stackexchange.com/questions/75785/how-to-generate-a-sequence-in-mysql
				-- This will generate a sequence from 1 to 1000.
				SELECT 
					(h*100+t*10+u+1) x 
				FROM
			 	(
			 		SELECT 0 h 
		 			UNION SELECT 1 
	 				UNION SELECT 2 
	 				UNION SELECT 3 
	 				UNION SELECT 4 
	 				UNION SELECT 5 
	 				UNION SELECT 6 
	 				UNION SELECT 7 
	 				UNION SELECT 8 
	 				UNION SELECT 9
 				) A,
			 	(
			 		SELECT 0 t 
			 		UNION SELECT 1 
			 		UNION SELECT 2 
			 		UNION SELECT 3 
			 		UNION SELECT 4 
			 		UNION SELECT 5 
			 		UNION SELECT 6 
			 		UNION SELECT 7 
			 		UNION SELECT 8 
			 		UNION SELECT 9
		 		) B,
			 	(
			 		SELECT 0 u 
			 		UNION SELECT 1 
			 		UNION SELECT 2 
			 		UNION SELECT 3 
			 		UNION SELECT 4 
			 		UNION SELECT 5 
			 		UNION SELECT 6 
			 		UNION SELECT 7 
			 		UNION SELECT 8 
			 		UNION SELECT 9
		 		) C
			) AS NumberGenerator
			WHERE
				x > 1
			ORDER BY
				x
		) AS Sequence,
		(
			SELECT 
				(h*100+t*10+u+1) x 
			FROM
		 	(
		 		SELECT 0 h 
	 			UNION SELECT 1 
 				UNION SELECT 2 
 				UNION SELECT 3 
 				UNION SELECT 4 
 				UNION SELECT 5 
 				UNION SELECT 6 
 				UNION SELECT 7 
 				UNION SELECT 8 
 				UNION SELECT 9
			) A,
		 	(
		 		SELECT 0 t 
		 		UNION SELECT 1 
		 		UNION SELECT 2 
		 		UNION SELECT 3 
		 		UNION SELECT 4 
		 		UNION SELECT 5 
		 		UNION SELECT 6 
		 		UNION SELECT 7 
		 		UNION SELECT 8 
		 		UNION SELECT 9
	 		) B,
		 	(
		 		SELECT 0 u 
		 		UNION SELECT 1 
		 		UNION SELECT 2 
		 		UNION SELECT 3 
		 		UNION SELECT 4 
		 		UNION SELECT 5 
		 		UNION SELECT 6 
		 		UNION SELECT 7 
		 		UNION SELECT 8 
		 		UNION SELECT 9
	 		) C
		) AS NumberGenerator2
		WHERE
			-- We just need to check numbers that are <= square root of the original number to be tested
			NumberGenerator2.x <= FLOOR(SQRT(Sequence.x))
		ORDER BY
			Sequence.x, NumberGenerator2.x
	) AS DivResults
	WHERE
		DivResults.DivMod = 0
	GROUP BY
		DivResults.OriginalNumber
	HAVING
		COUNT(*) = 1
) AS FinalResults
