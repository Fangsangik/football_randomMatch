-- 팀별 실제 멤버 수 계산 및 수정
UPDATE team t 
SET current_head_count = (
    SELECT COUNT(*) 
    FROM team_member tm 
    WHERE tm.team_id = t.id
);

-- 수정 결과 확인
SELECT 
    t.id,
    t.team_name,
    t.head_count,
    t.current_head_count,
    (SELECT COUNT(*) FROM team_member tm WHERE tm.team_id = t.id) AS actual_member_count
FROM team t;