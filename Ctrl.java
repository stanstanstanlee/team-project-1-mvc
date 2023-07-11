package ctrl;

import java.util.ArrayList;

import model.AnnounceDAO;
import model.AnnounceVO;
import model.MFunctionDAO;
import model.MFunctionVO;
import model.MemberDAO;
import model.MemberVO;
import view.AdminView;
import view.ClientView;
import view.CommonView;

public class Ctrl {

	//멤버변수
	private ClientView cView;
	private AdminView aView;
	private CommonView comView;
	private MemberDAO mDAO;
	private MemberVO member;
	private AnnounceDAO aDAO;
	private MFunctionDAO mfDAO;

	//생성자
	public Ctrl () {
		this.cView = new ClientView();
		this.aView = new AdminView();
		this.comView = new CommonView();
		this.aDAO = new AnnounceDAO();
		this.mDAO = new MemberDAO();
		this.mfDAO = new MFunctionDAO();
		this.member = null;
	}

	//메서드
	public void startApp () {
		AnnounceVO aVO = new AnnounceVO();
		ArrayList<AnnounceVO> adatas = new ArrayList<AnnounceVO>();
		aVO.setSerchCondition("전체출력");
		adatas = aDAO.selectAll(aVO);
		if(adatas.isEmpty()) {
			aVO.setSerchCondition("크롤링");
			if(!aDAO.insert(aVO)) {
				//System.out.println("log : crawling : false : insert fail");
			}
			else {
				//System.out.println("log : crawling : false : already exist");
			}
		}
		while(true) {
			boolean adminFlag = false;
			boolean clientFlag = false;
			// 프로그램 출력
			int maxNum = comView.printMenuLogin();
			int action = comView.getActionNum(maxNum);
			// 로그인
			if(action == 1) {
				MemberVO mVO=comView.signIn();
				member=mDAO.selectOne(mVO);				
				// 회원 정보 틀린 경우
				if(member.getMid()==null) {
					comView.loginFalse();
					continue;
				}
				// 관리자 로그인인 경우
				if(member.getIs_admin()==1) {
					aView.loginTrue();
					adminFlag = true;
				}
				// 이외에는 전부 회원 로그인
				else {
					cView.loginTrue();
					clientFlag = true;
				}
				while(true) {
					boolean exit = false;
					boolean logout = false;
					// 로그인하고 들어올때의 Flag에 따라 다른 메뉴 출력
					if(adminFlag) {
						maxNum = aView.printMenuAdmin();
					}else {
						maxNum = cView.printMenuClient();
					}
					action = comView.getActionNum(maxNum);
					// 로그아웃
					if(action==0) {
						member=null;
						logout = true;
					}
					// 채용정보
					else if(action==1) {
						int getANum = -1 ;
						aVO.setSerchCondition("전체출력");
						adatas = aDAO.selectAll(aVO);
						
						// 관리자는 출력하면 땡
						while(adminFlag) {
							comView.printAnnounceList(adatas);
							break;
						}
						// 회원 뷰는 여러 기능..
						while(clientFlag) {
							exit = false;
							cView.printArrayMethod(getANum);
							comView.printAnnounceList(adatas,getANum);
							cView.noticeArrayMethod();
							comView.noticeExitMethod();
							getANum =comView.getAnnounceNum(adatas);
							// 나가기 기능
							if(getANum == 0) {
								exit = true;
								break;
							}
							// 정렬 다시 출력
							else if (getANum < 0){
								continue;
							}
							// 일반 기능(지원하기,스크랩추가,나가기) +조회수
							else {
								aVO.setNum(adatas.get(getANum-1).getNum());
								aVO.setSerchCondition("조회수");
								if(!aDAO.update(aVO)) {
									comView.viewFalse();
									exit = true;
									break;
								}
								AnnounceVO adata = aDAO.selectOne(aVO);
								comView.printAnnounce(adata);
								int num = cView.printMenuAnnounce();
								// 0번 나가기 하는걸로..
								if (num == 0) {
									exit = true;
									break;
								}
								// 지원하기
								else if (num == 1) {
									MFunctionVO mfVO = new MFunctionVO(0,member.getMid(),adata.getNum(),null);
									mfVO.setSerchCondition("APPLY");
									if(!mfDAO.insert(mfVO)) {
										cView.applyFalse();
										break;
									}else {
										cView.applyTrue(adata);
										break;
									}
								}
								// 스크랩추가
								else if (num == 2) {
									MFunctionVO mfVO = new MFunctionVO(0,member.getMid(),adata.getNum(),null);
									mfVO.setSerchCondition("SCRAP");
									if(!mfDAO.insert(mfVO)) {
										cView.scrapFalse();
										break;
									}else {
										cView.scrapTrue(adata);
										break;
									}
								}
							}
						}
						if(exit) {
							comView.exitTrue();
							continue;
						}
						else {
							continue;
						}
					}
					// 검색하기 (이름검색)
					else if(action==2) {
						aVO = new AnnounceVO();
						aVO.setTitle(comView.getSearchContent());
						aVO.setSerchCondition("이름검색");
						adatas = aDAO.selectAll(aVO);
						comView.printAnnounceList(adatas);
						aVO = new AnnounceVO();
						continue;
					}
					// 회원 기능
					while (clientFlag) {
						exit = false;
						// 마이페이지
						if(action==3) {
							maxNum = cView.printMenuMypage();
							int num = comView.getActionNum(maxNum);
							// 지원 내역 
							if (num == 1 ) {
								MFunctionVO mfVO = new MFunctionVO();
								mfVO.setMid(member.getMid());
								mfVO.setSerchCondition("APPLY");
								ArrayList<MFunctionVO> mfdatas = mfDAO.selectAll(mfVO);
								if(!cView.printApplyList(mfdatas)) {
									continue;
								}
								comView.noticeExitMethod();
								num = cView.getCancelNum(mfdatas);
								if(num == 0) {
									exit = true;
									continue;
								}
								else {
									mfVO.setNum(mfdatas.get(num-1).getaNum());
									if(!mfDAO.delete(mfVO)) {
										cView.printWthdrwApplFalse();
										continue;
									}
									cView.printWthdrwApplTrue();
									continue;
								}

							}
							// 스크랩 내역 >> member selectAll
							else if (num == 2 ) {
								MFunctionVO mfVO = new MFunctionVO();
								mfVO.setMid(member.getMid());
								mfVO.setSerchCondition("SCRAP");
								ArrayList<MFunctionVO> mfdatas = mfDAO.selectAll(mfVO);
								if(!cView.printScrapList(mfdatas)) {
									continue;
								}
								comView.noticeExitMethod();
								num = cView.getCancelNum(mfdatas);
								if(num == 0) {
									exit = true;
									continue;
								}
								else {
									mfVO.setNum(mfdatas.get(num-1).getaNum());
									if(!mfDAO.delete(mfVO)) {
										cView.printCncelScrpFalse();
										continue;
									}
									cView.printCncelScrpTrue();
									continue;
								}
							}
							// 비밀번호 정보 변경 >> member update
							else if (num == 3) {

								if(!cView.checkMember(member)) {
									cView.checkFalse(); // 비밀번호 확인 실패
									continue;
								}
								String newMpw = cView.getNewMpw();
								member.setTmpMpw(newMpw);
								if(!mDAO.update(member)) {
									cView.setNewMpwFalse();
									continue;
								}
								member = null;
								cView.setNewMpwTrue();								
								logout = true;
								break;

							}
							// 회원 탈퇴
							else if (num == 4) {
								if(!cView.checkMember(member)) {
									cView.checkFalse();
									continue;
								}
								if(!mDAO.delete(member)) {
									cView.deleteFalse();
									continue;
								}
								member = null;
								cView.deleteTrue();
								logout = true;
								break;
							}
							// 마이페이지 나가기
							else if (num == 0) {
								exit = true;
								break;
							}							
						}
						break;
					}
					// 관리자 기능
					while (adminFlag) {
						// 회원목록 >> member selectAll
						if (action == 3) {
							ArrayList<MemberVO> mdatas = mDAO.selectAll(mVO);
							aView.printMemberList(mdatas);
						}
						// 공고등록 >> announce insert
						else if (action == 4) {
							aVO = aView.getAnnounceInfo();
							aVO.setSerchCondition("공고추가");
							if(!aDAO.insert(aVO)) {
								aView.insertAnnFalse();
								break;
							}
							aView.insertAnnTrue();
						}
						// 공고삭제 >> announce delete
						else if (action == 5 ) {
							aVO.setSerchCondition("전체출력");
							adatas = aDAO.selectAll(aVO);
							comView.printAnnounceList(adatas);
							comView.noticeExitMethod();
							int getANum = aView.getDelNum(adatas);
							if(getANum == 0) {
								exit = true;
								break;
							}
							aVO.setNum(adatas.get(getANum-1).getNum());
							aVO = aDAO.selectOne(aVO);

							if(!aDAO.delete(aVO)) {
								aView.deleteAnnFalse();
								break;
							}
							aView.deleteAnnTrue();
							break;
						}
						// 공고명수정 >> announce update
						else if (action == 6) {
							aVO.setSerchCondition("전체출력");
							adatas = aDAO.selectAll(aVO);
							comView.printAnnounceList(adatas);
							comView.noticeExitMethod();
							int getANum = aView.getChangeNum(adatas);
							if(getANum == 0) {
								exit = true;
								break;
							}
							aVO.setNum(adatas.get(getANum-1).getNum());
							aVO = aDAO.selectOne(aVO);
							comView.printAnnounce(aVO);
							String getNewName = aView.getNewName();
							aVO.setTitle(getNewName);
							aVO.setSerchCondition("공고명변경");
							if(!aDAO.update(aVO)) {
								aView.updateAnnFalse();
								break;
							}
							aView.updateAnnTrue();
						}
						break;
					}
					if(logout) {
						comView.logoutTrue();
						break;
					}
					if(exit) {
						comView.exitTrue();
						continue;
					}
				}
				continue;
			}
			// 회원가입
			else if(action == 2) {
				MemberVO mVO=comView.signUp();
				
				if(mVO==null) {
					comView.checkPwFalse();
					continue;			
				}
				boolean flag=mDAO.insert(mVO);
				if(flag) {
					comView.signUpTrue();
				}
				else {
					comView.signUpFalse();
				}
			}
			// 프로그램 종료
			else if(action == 0) {
				comView.programEndTrue();
				break;
			}

		}
	}
}
