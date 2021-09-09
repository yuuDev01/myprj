package com.example.myprj.domain.common.dao;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.myprj.domain.common.dto.UpLoadFileDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UpLoadFIleDAOImpl implements UpLoadFileDAO {
	private final JdbcTemplate jt;
	
	@Override
	public void addFile(UpLoadFileDTO uploadFileDTO) {
		List<UpLoadFileDTO> list = new ArrayList<>();
		list.add(uploadFileDTO);
		addFiles(list);
	}
	
	@Override
	public void addFiles(List<UpLoadFileDTO> list) {
		
		StringBuffer sql = new StringBuffer();
		sql.append("insert into uploadfile ( ");
		sql.append("  fid, ");
		sql.append("  rid, ");
		sql.append("  code, ");
		sql.append("  store_fname,  ");
		sql.append("  upload_fname, ");
		sql.append("  fsize,  ");
		sql.append("  ftype ");
		sql.append(") ");
		sql.append("values( ");
		sql.append("  uploadfile_fid_seq.nextval, ");
		sql.append("  ?, ");
		sql.append("  ?, ");
		sql.append("  ?, ");
		sql.append("  ?, ");
		sql.append("  ?, ");
		sql.append("  ? ");
		sql.append(") ");		
		
		jt.batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, list.get(i).getRid());
				ps.setString(2, list.get(i).getCode());
				ps.setString(3, list.get(i).getStore_fname());
				ps.setString(4, list.get(i).getUpload_fname());
				ps.setString(5, list.get(i).getFsize());
				ps.setString(6, list.get(i).getFtype());
			}
			
			@Override
			public int getBatchSize() {
				return list.size();
			}
		});
		
	}

	@Override
	public List<UpLoadFileDTO> getFiles(String rid) {
		
		StringBuffer sql = new StringBuffer();
		sql.append("select fid,rid,code,store_fname,upload_fname, ");
		sql.append("       fsize,ftype,cdate,udate ");
		sql.append("  from uploadfile  ");
		sql.append(" where rid = ? ");
		
		List<UpLoadFileDTO> list = 
				jt.query( sql.toString(), 
									new BeanPropertyRowMapper<>(UpLoadFileDTO.class), 
									rid );
		
		return list;
	}

	@Override
	public List<UpLoadFileDTO> getFiles(String rid, String code) {

		StringBuffer sql = new StringBuffer();
		sql.append("select fid,rid,code,store_fname,upload_fname, ");
		sql.append("       fsize,ftype,cdate,udate ");
		sql.append("  from uploadfile  ");
		sql.append(" where rid  = ? ");		
		sql.append("   and code = ? ");		

		List<UpLoadFileDTO> list = 
				jt.query( sql.toString(), 
									new BeanPropertyRowMapper<>(UpLoadFileDTO.class), 
									rid, code );
		
		return list;
	}
	
	@Override
	public UpLoadFileDTO getFileByFid(String fid) {
		StringBuffer sql = new StringBuffer();
		sql.append("select fid,rid,code,store_fname,upload_fname, ");
		sql.append("       fsize,ftype,cdate,udate ");
		sql.append("  from uploadfile  ");
		sql.append(" where fid  = ? ");
		
		return jt.queryForObject(
							sql.toString(), 
							new BeanPropertyRowMapper<>(UpLoadFileDTO.class),
							fid);
	}

	@Override
	public UpLoadFileDTO getFileBySfname(String sfname) {
		StringBuffer sql = new StringBuffer();
		sql.append("select fid,rid,code,store_fname,upload_fname, ");
		sql.append("       fsize,ftype,cdate,udate ");
		sql.append("  from uploadfile  ");
		sql.append(" where store_fname  = ? ");
		
		return jt.queryForObject(
							sql.toString(), 
							new BeanPropertyRowMapper<>(UpLoadFileDTO.class),
							sfname);
	}
	
	/**
	 * 첨부파일 삭제 by rid
	 */
	@Override
	public void deleteFileByRid(String rid) {
		String sql = "delete from uploadfile where rid = ? ";
		jt.update(sql, rid);
	}

	/**
	 * 첨부파일 삭제 by rid, code
	 */
	@Override
	public void deleteFileByRid(String rid, String code) {
		String sql = "delete from uploadfile where rid = ? and code = ? ";
		jt.update(sql, rid, code);
	}
	
	/**
	 * 서버보관 파일명 가져오기 by rid
	 */
	@Override
	public List<String> getStore_Fname(String rid) {
		
		String sql = "select store_fname from uploadfile where rid = ? ";
		
		List<String> store_fnames = 
				jt.queryForList(sql, String.class, rid);
		
		return store_fnames;
	}
	
	//파일삭제 by fid
	@Override
	public void deleteFileByFid(String fid) {
		
		String sql = "delete from uploadfile where fid = ? ";
		
		jt.update(sql, fid);
		
	}
	
	//파일삭제 by sfname
	@Override
	public void deleteFileBySfname(String sfname) {
		String sql = "delete from uploadfile where store_fname = ? ";
		
		jt.update(sql, sfname);
		
	}
}
