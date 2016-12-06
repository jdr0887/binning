


-- index drops
DROP INDEX var.asm_loc_var_loc_var_id_idx;
DROP INDEX var.asm_loc_var_asm_id_idx;
DROP INDEX dbsnp.snp_mapping_loc_var_id_idx;
DROP INDEX dbsnp.snp_mapping_loc_var_id_idx1;
DROP INDEX hgmd.hgmd_loc_var_loc_var_id_idx;

-- index creates
CREATE INDEX CONCURRENTLY variants_61_2_gene_id_idx ON refseq.variants_61_2 (gene_id);
CREATE INDEX CONCURRENTLY hgmd_loc_var_loc_var_id_idx ON hgmd.hgmd_loc_var (loc_var_id);
CREATE INDEX CONCURRENTLY hgmd_loc_var_v1_loc_var_id_idx ON hgmd.hgmd_loc_var_v1 (loc_var_id);

-- adding foriegn keys constraints
ALTER TABLE refseq.variants_61_2 ADD FOREIGN KEY (gene_id) REFERENCES annot.gene (gene_id) DEFERRABLE;
ALTER TABLE refseq.variants_61_2 ADD FOREIGN KEY (variant_effect) REFERENCES refseq.variant_effect (variant_effect) DEFERRABLE;
ALTER TABLE refseq.transcr_maps ALTER COLUMN strand TYPE varchar(1);
ALTER TABLE clinbin.bin_results_final_diagnostic ALTER COLUMN tag TYPE varchar(5) USING tag::character varying(5);
ALTER TABLE hgmd.hgmd_loc_var ALTER COLUMN tag TYPE varchar(5) USING tag::character varying(5);
ALTER TABLE hgmd.deletion_cgquick ALTER COLUMN tag TYPE varchar(5) USING tag::character varying(5);
ALTER TABLE hgmd.hgmd_af ALTER COLUMN tag TYPE varchar(5) USING tag::character varying(5);
ALTER TABLE hgmd.insertion_cgquick ALTER COLUMN tag TYPE varchar(5) USING tag::character varying(5);
ALTER TABLE hgmd.mutation ALTER COLUMN tag TYPE varchar(5) USING tag::character varying(5);
ALTER TABLE hgmd.sub_cgquick ALTER COLUMN tag TYPE varchar(5) USING tag::character varying(5);
ALTER TABLE hgmd.hgmd_all_t ALTER COLUMN tag TYPE varchar(5) USING tag::character varying(5);
ALTER TABLE hgmd.hgmd_annot ALTER COLUMN tag TYPE varchar(5) USING tag::character varying(5);
ALTER TABLE hgmd.hgmd_annot_old ALTER COLUMN tag TYPE varchar(5) USING tag::character varying(5);
ALTER TABLE hgmd.hgmd_loc_var_test ALTER COLUMN tag TYPE varchar(5) USING tag::character varying(5);
ALTER TABLE hgmd.hgmd_loc_var_v1 ALTER COLUMN tag TYPE varchar(5) USING tag::character varying(5);
ALTER TABLE hgmd.hgmd_snp_t ALTER COLUMN tag TYPE varchar(5) USING tag::character varying(5);
ALTER TABLE hgmd.hgmd_snp_t ALTER COLUMN tag TYPE varchar(5) USING tag::character varying(5);
ALTER TABLE clinbin.diagnostic_binning_job ALTER COLUMN gender TYPE varchar(1);
ALTER TABLE clinbin.incidental_binning_job ALTER COLUMN gender TYPE varchar(1);



ALTER TABLE clinbin.max_freq ALTER COLUMN max_allele_freq DROP NOT NULL;
ALTER TABLE clinbin.max_freq ALTER COLUMN source DROP NOT NULL;

-- casts for postgres enums to work as primitives
--CREATE OR REPLACE FUNCTION hgmd_enum_to_varchar(hgmd.hgmd_enum) RETURNS varchar STRICT IMMUTABLE LANGUAGE SQL AS 'select cast($1 as varchar);';
--CREATE OR REPLACE FUNCTION refseq_strand_to_varchar(refseq.strand_type) RETURNS varchar STRICT IMMUTABLE LANGUAGE SQL AS 'select cast($1 as varchar);';
--CREATE OR REPLACE FUNCTION clinbin_varchar_to_gender(varchar) RETURNS clinbin.gender STRICT IMMUTABLE LANGUAGE SQL AS 'select cast($1 as clinbin.gender);';

CREATE FUNCTION binning_job_insert(_incidental_list_version integer, _diagnostic_list_version integer, _participant text, _bin_start timestamp with time zone, _bin_stop timestamp with time zone, _gender varchar(1), _select_bin_1 boolean, _select_bin_2a boolean, _select_bin_2b boolean, _select_bin_2c boolean, _dx_id integer, _status text, _failure_message text, _select_carrier boolean) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    _id integer;
BEGIN
    INSERT INTO clinbin.binning_job (incidental_list_version, diagnostic_list_version, participant, bin_start, bin_stop, gender,
                                     select_bin_1, select_bin_2a, select_bin_2b, select_bin_2c, dx_id, status, failure_message, select_carrier)
        VALUES (_incidental_list_version, _diagnostic_list_version, _participant, _bin_start, _bin_stop, _gender,
                _select_bin_1, _select_bin_2a, _select_bin_2b, _select_bin_2c, _dx_id, _status, _failure_message, _select_carrier)
        RETURNING binning_job_id
        INTO _id;

    RETURN _id;
END;
$$;

ALTER FUNCTION clinbin.binning_job_insert(_incidental_list_version integer, _diagnostic_list_version integer, _participant text, _bin_start timestamp with time zone, _bin_stop timestamp with time zone, _gender varchar(1), _select_bin_1 boolean, _select_bin_2a boolean, _select_bin_2b boolean, _select_bin_2c boolean, _dx_id integer, _status text, _failure_message text, _select_carrier boolean) OWNER TO vardb_admin;

CREATE FUNCTION binning_job_insert(_incidental_list_version integer, _diagnostic_list_version integer, _participant text, _bin_start timestamp with time zone, _bin_stop timestamp with time zone, _gender varchar(1), _select_bin_1 boolean, _select_bin_2a boolean, _select_bin_2b boolean, _select_bin_2c boolean, _dx_id integer, _status text, _failure_message text, _select_carrier boolean, _vcf_file text) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    _id integer;
BEGIN
    INSERT INTO clinbin.binning_job (incidental_list_version, diagnostic_list_version, participant, bin_start, bin_stop, gender,
                                     select_bin_1, select_bin_2a, select_bin_2b, select_bin_2c, dx_id, status, failure_message, select_carrier, vcf_file)
        VALUES (_incidental_list_version, _diagnostic_list_version, _participant, _bin_start, _bin_stop, _gender,
                _select_bin_1, _select_bin_2a, _select_bin_2b, _select_bin_2c, _dx_id, _status, _failure_message, _select_carrier, _vcf_file)
        RETURNING binning_job_id
        INTO _id;

    RETURN _id;
END;
$$;

ALTER FUNCTION clinbin.binning_job_insert(_incidental_list_version integer, _diagnostic_list_version integer, _participant text, _bin_start timestamp with time zone, _bin_stop timestamp with time zone, _gender varchar(1), _select_bin_1 boolean, _select_bin_2a boolean, _select_bin_2b boolean, _select_bin_2c boolean, _dx_id integer, _status text, _failure_message text, _select_carrier boolean, _vcf_file text) OWNER TO vardb_admin;

CREATE FUNCTION diagnostic_binning_job_insert(_diagnostic_list_version integer, _participant text, _gender varchar(1), _dx_id integer, _study text) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    _id integer;
BEGIN
    INSERT INTO clinbin.diagnostic_binning_job (diagnostic_list_version, participant,  gender, dx_id, status, failure_message, vcf_file, study)
        VALUES ( _diagnostic_list_version, _participant,  _gender, _dx_id, 'Requested', '', '', _study)
        RETURNING binning_job_id
        INTO _id;

    RETURN _id;
END;
$$;

ALTER FUNCTION clinbin.diagnostic_binning_job_insert(_diagnostic_list_version integer, _participant text, _gender varchar(1), _dx_id integer, _study text) OWNER TO vardb_admin;

CREATE FUNCTION incidental_binning_job_insert(_incidental_list_version integer, _participant text, _gender varchar(1), _incidental_bin_id integer, _study text) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    _id integer;
BEGIN
    INSERT INTO clinbin.incidental_binning_job (incidental_list_version, participant, gender, incidental_bin_id, status, failure_message, vcf_file, study)
        VALUES ( _incidental_list_version, _participant,  _gender, _incidental_bin_id, 'Requested', '','', _study)
        RETURNING binning_job_id
        INTO _id;

    RETURN _id;
END;
$$;

ALTER FUNCTION clinbin.incidental_binning_job_insert(_incidental_list_version integer, _participant text, _gender text, _incidental_bin_id integer, _study text) OWNER TO vardb_admin;

