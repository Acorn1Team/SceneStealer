package pack.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import pack.review.ReviewDto;



public class MainMgr {
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private DataSource ds;

	// DB 연결을 위한 생성자
	public MainMgr() {
		try {
			Context context = new InitialContext();
			ds = (DataSource) context.lookup("java:comp/env/jdbc_maria");
		} catch (Exception e) {
			System.out.println("DB CONNECT ERROR : " + e.getMessage());
		}
	}
	
	// 메인에서 보일 시리즈 받아오기
	public ArrayList<SeriesDto> getSeriesDataforMain() {
		ArrayList<SeriesDto> list = new ArrayList<SeriesDto>();
		try {
			conn = ds.getConnection();
			String sql = "SELECT s.series_title, s.series_pic, s.series_num, sum(c.character_like) AS cl FROM series AS s INNER JOIN `character` AS c ON s.series_num = c.series_num GROUP BY series_title ORDER BY cl DESC";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			
			while (rs.next()) {
				SeriesDto dto = new SeriesDto();
				dto.setTitle(rs.getString("series_title"));
				dto.setPic(rs.getString("series_pic"));
				dto.setNum(rs.getInt("series_num"));
				list.add(dto);
			}
		}catch (Exception e) {
			System.out.println("getSeriesDataAll() ERROR : " + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				System.out.println("getSeriesDataAll() - finally ERROR : " + e2.getMessage());
			}
		}
		
		return list;
	}
	
	// 메인에서 보일 리뷰 받아오기 (LIMIT 3)
	public ArrayList<ReviewDto> getReviewDataAll() {
		ArrayList<ReviewDto> list = new ArrayList<ReviewDto>();
		try {
			conn = ds.getConnection();
			String sql = "SELECT * FROM review ORDER BY review_num DESC LIMIT 3";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ReviewDto dto = new ReviewDto();
				dto.setUser(rs.getString("user_id"));
				dto.setProduct(rs.getString("product_name"));
				dto.setContents(rs.getString("review_contents"));
				dto.setPic(rs.getString("review_pic"));
				list.add(dto);
			}
		}catch (Exception e) {
			System.out.println("getReviewDataAll() ERROR : " + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				System.out.println("getReviewDataAll() - finally ERROR : " + e2.getMessage());
			}
		}
		
		return list;
	}
	
	// 검색
	public ArrayList<SeriesDto> searchSeries(String searchword, String searchSelect) {
		ArrayList<SeriesDto> list = new ArrayList<SeriesDto>();
		
		try {
			String sql = null;
			conn = ds.getConnection();
			if (searchSelect.equals("series")) {
				// 작품 검색일 경우 SQL문
				sql = "SELECT * FROM series WHERE series_title LIKE ?";				
			} else if (searchSelect.equals("actor")) {
				// 배우 검색일 경우 SQL문
				sql = "SELECT * FROM series WHERE series_num IN (SELECT series_num FROM series_actor WHERE actor_num IN (SELECT actor_num FROM actor WHERE actor_name LIKE ?))";				
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + searchword + "%");
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				SeriesDto dto = new SeriesDto();
				dto.setPic(rs.getString("series_pic"));
				dto.setNum(rs.getInt("series_num"));
				dto.setTitle(rs.getString("series_title"));
				list.add(dto);
			}
			
		}catch (Exception e) {
			System.out.println("searchSeries() ERROR : " + e);
			list = null;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				System.out.println("searchSeries() - finally ERROR : " + e2.getMessage());
			}
		}
		
		return list;
	}
	
	// 시리즈 번호로 캐릭터 정보 받아오기
	public ArrayList<CharacterDto> getCharacterData(String series_num) {
		ArrayList<CharacterDto> list = new ArrayList<CharacterDto>();
		
		
		try {
			conn = ds.getConnection();
			String sql = "SELECT * FROM `character` WHERE series_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, series_num);
//			pstmt.setInt(2, character_num);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				CharacterDto dto = new CharacterDto();
				dto.setPic(rs.getString("character_pic"));
				dto.setName(rs.getString("character_name"));
				dto.setLike(rs.getInt("character_like"));
				dto.setNum(rs.getInt("character_num"));
				list.add(dto);
			}
		} catch (Exception e) {
			System.out.println("getCharacterData() ERROR : " + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				System.out.println("getCharacterData() - finally ERROR : " + e2.getMessage());
			}
		}
		return list;
	}
	
	// 캐릭터 번호로 스타일 정보 받아오기
	public ArrayList<StyleDto> getStyleData(int character_num) {
		ArrayList<StyleDto> list = new ArrayList<StyleDto>();
		
		try {
			conn = ds.getConnection();
			String sql = "SELECT * FROM style WHERE character_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, character_num);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				StyleDto dto = new StyleDto();
				dto.setNum(rs.getInt("style_num"));
				dto.setPic(rs.getString("style_pic"));
				list.add(dto);
			}
		} catch (Exception e) {
			System.out.println("getStyleData() ERROR : " + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				System.out.println("getStyleData() - finally ERROR : " + e2.getMessage());
			}
		}
		
		return list;
	}
	
	
	// 스타일 번호로 아이템 정보 받아오기
	public ArrayList<ItemDto> getItemData(int style_num) {
		ArrayList<ItemDto> list = new ArrayList<ItemDto>();
		
		try {
			conn = ds.getConnection();
			String sql = "SELECT * FROM item WHERE style_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, style_num);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ItemDto dto = new ItemDto();
				dto.setPic(rs.getString("item_pic"));
				dto.setNum(rs.getInt("item_num"));
				list.add(dto);
			}
		} catch (Exception e) {
			System.out.println("getItemData() ERROR : " + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				System.out.println("getItemData() - finally ERROR : " + e2.getMessage());
			}
		}
		
		return list;
	}
	
	// 시리즈 번호와 캐릭터 이름으로 해당하는 캐릭터의 정보만 가져오기
	public CharacterDto getCharacterByName(String num, String name) {
		CharacterDto dto = null;
		
		try {
			conn = ds.getConnection();
			String sql = "SELECT * FROM `character` WHERE series_num = ? AND character_name = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, num);
			pstmt.setString(2, name);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				dto = new CharacterDto();
				dto.setPic(rs.getString("character_pic"));
				dto.setName(rs.getString("character_name"));
				dto.setLike(rs.getInt("character_like"));
				dto.setNum(rs.getInt("character_num"));
			}
		} catch (Exception e) {
			System.out.println("getCharacterByName() ERROR : " + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				System.out.println("getCharacterByName() - finally ERROR : " + e2.getMessage());
			}
		}
		
		return dto;
	}
	
	

}