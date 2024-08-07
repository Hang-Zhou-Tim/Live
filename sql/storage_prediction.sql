select
    table_schema as 'database',
    table_name as 'table name',
    table_rows as 'row name',
    truncate(data_length/1024/1024,2) as 'data storage used(MB)',
    truncate(index_length/1024/1024,2) as 'index storage used(MB)'
    from information_schema.tables
    where table_schema='live_user'
    order by data_length desc, index_length desc;
