select b.* from book b join author a on a.id = b.author_id
where a.name = ?1;
