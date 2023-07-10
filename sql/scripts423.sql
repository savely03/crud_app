-- 1 Step
SELECT s.name student_name,
       s.age student_age,
       f.name faculty_name
    FROM student s
        join faculty f
            on (s.faculty_id = f.id);

-- 2 Step
SELECT s.name student_name,
       s.age student_age,
       a.file_path avatar_filepath
    FROM student s
        join avatar a
            on (s.id = a.student_id)
