CREATE TABLE IF NOT EXISTS public.parsed_xml_details (
    id SERIAL PRIMARY KEY,
    newspaper_name character varying(70) NOT NULL,
    file_name character varying(70) NOT NULL,
    upload_time timestamp without time zone NOT NULL,
    width integer not null,
    height integer not null,
    dpi integer not null
);