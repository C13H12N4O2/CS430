-- a)
--DROP TABLE Works;
--DROP TABLE Department;
--DROP TABLE Employee;

CREATE TABLE Employee (eid integer PRIMARY KEY,
                    		ename varchar(20),
                    		age integer,
                    		salary number(8,2));

CREATE TABLE Department (did integer PRIMARY KEY,
                    		dname varchar(20),
					budget number(8,2),
                    		managerid integer,
					FOREIGN KEY (managerid) REFERENCES Employee(eid));

CREATE TABLE Works (eid integer,
                     	did integer,
                      	pct_time integer,
                      	PRIMARY KEY(eid, did),
                      	FOREIGN KEY (eid) REFERENCES Employee,
                      	FOREIGN KEY (did) REFERENCES Department);

-- b)
SELECT E.salary
FROM Employee E, Works W, Department D
WHERE D.did = W.did AND W.eid = E.eid AND D.dname LIKE 'Mar%';

-- c)
SELECT DISTINCT E.age
FROM Employee E, Works W
WHERE E.eid = W.eid AND W.pct_time >= 30;

-- d) 
SELECT DISTINCT E.salary
FROM Employee E, Works W
WHERE E.eid = W.eid AND E.eid NOT IN (
			  SELECT W1.eid
                   	  FROM Works W1, Department D1
			  WHERE W1.did=D1.did AND D1.budget <= 500000
		          );

-- e)
SELECT E.ename
FROM Employee E, Department D
WHERE E.eid = D.managerid;

-- f)
SELECT AVG(E.salary)
FROM Employee E;

-- g)
SELECT E.age
FROM Employee E, Works W, Department D
WHERE E.eid = W.eid AND W.did = D.did
      AND D.dname = 'Catering' 	AND W.pct_time >= 10 
      AND E.eid NOT IN (
                  SELECT W1.eid
                  FROM Works W1, Department D1
		  WHERE W1.did=D1.did AND D1.budget > 500000
		  );

-- h)
SELECT E.ename
FROM Employee E
WHERE NOT EXISTS (
	SELECT D.did FROM Department D WHERE D.budget > 500000
	MINUS
	SELECT W.did FROM Works W WHERE W.eid = E.eid);	


-- i)
SELECT D.dname
FROM Department D
WHERE D.budget = (SELECT MAX(D1.budget) FROM Department D1);

-- j)
SELECT D.did, MAX(E.salary)
FROM Employee E, Works W, Department D
WHERE E.eid = W.eid AND W.did = D.did AND E.age <= 30
GROUP BY D.did
HAVING 10 <= (SELECT COUNT(*) FROM Works W1 WHERE W1.did=D.did);

-- k)
SELECT D.managerid, AVG(E.salary)
FROM Employee E, Works W, Department D
WHERE E.eid = W.eid AND W.did = D.did
GROUP BY D.managerid;

-- l)
SELECT D.did, AVG(E.age)
FROM Employee E, Works W, Department D
WHERE E.eid = W.eid AND W.did = D.did
GROUP BY D.did
HAVING 0 = (
	SELECT COUNT(*) 
	FROM Employee E1, Works W1
	WHERE E1.eid=W1.eid AND W1.did = D.did AND E1.age > 30
	);

-- m)
SELECT TMP.dname
FROM (
	SELECT D.did, D.dname, AVG(E.age) AS avgage
	FROM Employee E, Works W, Department D
	WHERE E.eid = W.eid AND W.did = D.did
	GROUP BY D.did, D.dname
	) TMP
WHERE TMP.avgage = (
	SELECT MAX(TMP2.avgage) FROM
		(
		SELECT D.did, D.dname, AVG(E.age) AS avgage
		FROM Employee E, Works W, Department D
		WHERE E.eid = W.eid AND W.did = D.did
       		GROUP BY D.did, D.dname
		) TMP2
	);


-- n)
SELECT temp.age
FROM (SELECT E.age, COUNT(DISTINCT E.eid) AS count
      FROM Employee E, Works W, Department D
      WHERE E.eid = W.eid AND W.did = D.did AND D.budget > 300000
      GROUP BY E.age) temp
WHERE temp.count = (SELECT MAX(temp2.count)
      FROM (SELECT E.age, COUNT(DISTINCT E.eid) AS count
      	 FROM Employee E, Works W, Department D
      	 WHERE E.eid = W.eid AND W.did = D.did AND D.budget > 300000
      	 GROUP BY E.age) temp2
      );




-- o)
SELECT AVG(E.salary)
FROM Employee E
WHERE E.eid IN
	(
	SELECT E1.eid FROM Employee E1
	WHERE NOT EXISTS(
		SELECT D.did FROM Department D
			WHERE D.dname LIKE 'Ca%'
		MINUS
		SELECT W.did FROM Works W
			WHERE W.eid = E1.eid
	)
	);
