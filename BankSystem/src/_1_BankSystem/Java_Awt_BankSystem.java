package _1_BankSystem;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.html.ListView;

// BankSystem - by. 최 한규

public class Java_Awt_BankSystem extends Frame implements ActionListener, Runnable{
	boolean typeChange; // 로그인 <-> 로그아웃 변환
	private List<Bank_Account> accountArray;   // 2017-01-23 modify Array → ArrayList 
	public JButton[] button;	// 메인 버튼
	   /* Main Page Button
	  	button[0] : Register Button	 -> 0
	  	button[1] : login Button	 -> 1
	  	button[2] : Deposit Button	 -> 2
	  	button[3] : Withdraw Button	 -> 3
	  	button[4] : Search Button	 -> 4
	  	button[5] : Exit Button		 -> 5
		*/
   private JButton button_0_join, button_0_cancel, button_0_idCheck, button_1_ok, button_1_cancel,
                   button_2_ok, button_2_cancel, button_3_ok,button_3_cancel, button_4_cancel, b_logout_ok;
   public JPanel[] panel;
	   /* CardLayout Main Panel 
	 	panel[0] : Main Panel 		-> 0
	 	panel[1] : Register Panel 	-> 1
	 	panel[2] : login Panel		-> 2
	 	panel[3] : Deposit Panel	-> 3
	 	panel[4] : Withdraw Panel 	-> 4
	 	panel[5] : Search Panel 	-> 5
		*/
   private JPanel panel_1_1, panel_2_1, panel_3_1, panel_3_2,
   			      panel_4_1, panel_4_2, panel_5_1, panel_5_2;
   public JLabel[] label; // 공백 라벨 배열화
   public JLabel label_id, label_name, label_pw, label_pw2, label_startbalance, label_search, label_login_id, 
   				 label_login_pw, label_deposit_money, label_withdraw_money;
   private JTextField tField_id, tField_name, tField_balance, tf_login_id, tf_deposit_money, tf_withdraw_money;
   public ImageIcon icon;
   public CardLayout cards;
   public Dialog[] dialog; // Dialog 반복 구문 배열화
   
   public String[] path; // 메인 버튼 이미지 상대주소 저장
   public JPasswordField pwTf, pwTf2, pf_login_pw;
   public JTable table;
   public DefaultTableModel model;
   public JScrollPane sPanel;
   Thread t1;

   // 메소드 line 표시 ########################################################
   /* last update date : 2017/02/07
      
     0. Java_Awt_BankSystem() - line : 103 
     1. ThreadState Method - loginState  line : 429
     2. Button Action Method - actionPerformed  line : 439
   	 3. Register Method - register  line : 503
   	 4. Register Id Check Method - checkMember  line : 531
   	 5. Register Id Button Overlap Clicked Method - checkId  line : 542
   	 6. Login Method - login  line : 553
   	 7. Login Auth Button Clicked Method - loginAuth  line : 571
   	 8. Search Method - accountList  line : 589
   	 9. Deposit Method - deposit  line : 601
   	10. 입,출금 시 사용자 정보 가져오기 Method - checkDeposit  line : 619
   	11. Withdraw Method - withdraw  line : 632
   	12. 타이틀 쓰레드 Method - titleMove  line : 661
   	13. Main Method - main  line : 698
   	  
   */
   //#####################################################################
   
   // Java_Awt_BankSystem() 
   public Java_Awt_BankSystem() throws IOException {
	  t1 = new Thread(this);
	  t1.start();	// 쓰레드 실행 ( 타이틀 액션 )
	  typeChange = true;

      accountArray = new ArrayList<Bank_Account>();   // 2017-01-23 modify Array → ArrayList  
      
      // 회원정보 가져오기 파일이 없으면 FileNotFoundException , 회원정보 불러오기를 건너뛰고 프로그램 시작
      String addName = ""; String addId = ""; String addPw = ""; String addBal =""; String str = ""; long addBal2;
      try {
    	  FileReader freader = new FileReader(new File("D:/Hangyu_Info.txt"));
    	  while(freader.ready()){
    		  char ch = (char) freader.read();
    		  str = str + ch;
    	  }
    	  freader.close();
      } catch (IOException e1) {
    	  e1.printStackTrace();
	  } 

	  StringTokenizer st = new StringTokenizer(str, "|");
		 
	  // 가저온 회원정보를 accountArray에 담기
	  while(st.hasMoreTokens()) {
		  addId = st.nextToken();
		  System.out.println("아이디 : "+ addId + " 님의 정보 등록");
		  addName = st.nextToken();
		  addPw = st.nextToken();
		  addBal = st.nextToken();
		  addBal2 =(long) Integer.parseInt(addBal);
		  Bank_Account newAccount = new Bank_Account(addId, addName, addPw, addBal2);
		  accountArray.add(newAccount);
	  } 	

      Vector<String> Column = new Vector<String>();	// 컬렉션 List - Vector 사용
      // 열 추가 
	  Column.addElement("아이디");	Column.addElement("이름"); Column.addElement("잔액");
		 
	  model = new DefaultTableModel(Column, 0){ // 테이블 모델 생성 
		  public boolean isCellEditable(int row, int column){ // 수정 금지
 		     return false;
 		  }
	  };
      table = new JTable(model); // 테이블 생성
      cards = new CardLayout();
      setLayout(cards);
      setLocation(700, 300);

      // CardLayout View Panel 정의 ㅡㅡㅡㅡㅡㅡㅡ
      
      panel = new JPanel[6];
      for(int i = 0; i < panel.length; i++) {
    	  panel[i] = new JPanel();
      }
      panel[0] = new JPanel();	/*메인 */
      panel[1] = new JPanel();	/*회원가입*/
      panel[2] = new JPanel();	/*로그인 */
      panel[3] = new JPanel();  /*입금 */
      panel[4] = new JPanel();	/*출금 */
      panel[5] = new JPanel();	/*조회 */
      //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

      // Label[] 공백 정의 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      label = new JLabel[40];
      for (int i = 0; i < label.length; i++) { label[i] = new JLabel(" "); }
      //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
       
      /* Dialog 정의 */		/* Dialog 반복문 적용 */ 
      // 2017-02-06 적용 완료
      dialog = new Dialog[25];
      for (int i = 0; i < dialog.length; i++) { dialog[i] = new MyDialog(this); dialog[i].setLayout(new FlowLayout()); dialog[i].setLocation(800, 500); }
      // 공백 체크 - Dialog ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      dialog[0].add(new JLabel("공백은 안됩니다."));
      // 회원가입 완료 - Dialog ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      dialog[1].add(new JLabel("회원가입이 완료 되었습니다."));
      // 회원가입 취소 - Dialog ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      dialog[2].add(new JLabel("회원가입이 취소 되었습니다."));
      // 정보조회 취소 - Dialog ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      dialog[3].add(new JLabel("조회를 종료합니다."));
      // 로그인 완료 - Dialog ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      dialog[4].add(new JLabel("로그인 되셨습니다."));
      // 로그인 취소 - Dialog ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      dialog[5].add(new JLabel("메인화면으로 돌아갑니다."));
      // 회원이 아무도 없을 때 로그인 - Dialog ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      dialog[6].add(new JLabel("회원가입을 해주세요."));
      // 아이디 틀림 - Dialog ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      dialog[7].add(new JLabel("아이디가 존재하지 않습니다."));
      // 아이디 사용가능 - Dialog ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      dialog[8].add(new JLabel("사용가능 한 아이디 입니다."));
      // 아이디가 중복 - Dialog ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      dialog[9].add(new JLabel("아이디가 중복 됩니다."));
      // 아이디 가 공백 - Dialog ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      dialog[10].add(new JLabel("아이디를 입력하세요."));
      // 비밀번호 체크 - Dialog ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      dialog[11].add(new JLabel("비밀번호가 맞지 않습니다. 다시 입력해주세요."));
      // 이름 공백일 떄  - Dialog ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      dialog[12].add(new JLabel("이름을 입력 해주세요."));
      // 암호 공백일 떄  - Dialog ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      dialog[13].add(new JLabel("비밀번호를 입력 해주세요."));
      // 암호 재확인이 공백일 떄  - Dialog ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      dialog[14].add(new JLabel("비밀번호를 한번 더 입력해주세요."));
      // 전체적으로 공백일 때  - Dialog ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      dialog[15].add(new JLabel("모든 정보를 입력 해주세요."));
      // 입금 금액이 공백일 때 - Dialog ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      dialog[16].add(new JLabel("입금하실 금액을 입력 해주세요."));
  	  // 출금 금액이 공백일 때 - Dialog ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      dialog[17].add(new JLabel("출금하실 금액을 입력 해주세요."));
      // 잔액 부족 - Dialog ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ   
      dialog[18].add(new JLabel("잔액이 부족합니다. 다시 입력해주세요."));
      // 입금 & 출금 성공 - Dialog ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      dialog[19].add(new JLabel("이용해 주셔서 감사합니다."));
      
      for (int i = 0; i < dialog.length; i++) {	dialog[i].pack(); } // 문장길이 만큼 사이즈를 늘림
      //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
       
      /* Label, TextField 정의  */                                    
      // 회원가입 페이지  ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      label_id = new JLabel("아이디"); tField_id = new JTextField(); 
      label_name = new JLabel("이름"); tField_name = new JTextField();
      label_pw = new JLabel("비밀번호 "); pwTf = new JPasswordField();
      label_pw2 = new JLabel("비밀번호 재확인 "); pwTf2 = new JPasswordField();
      label_startbalance = new JLabel("초기 예금액 "); tField_balance = new JTextField();
      //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      // 로그인 페이지 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      label_login_id = new JLabel("아이디"); tf_login_id = new JTextField();
      label_login_pw = new JLabel("비밀번호"); pf_login_pw = new JPasswordField();
      //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      // 입금 페이지  ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      label_deposit_money = new JLabel("입금하실 금액 "); tf_deposit_money = new JTextField();
      //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      // 출금 페이지 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      label_withdraw_money = new JLabel("출금하실 금액 "); tf_withdraw_money = new JTextField();
      //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      
      
      /* Button 정의 */ 
      // 메인 페이지 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      
      path = new String[6];	// 버튼 이미지 상대주소
      path[0] = "img/_register_new.png"; // Register
      path[1] = "img/_login_new.png";	 // Login
      path[2] = "img/_deposit_new.png";	 // Deposit
      path[3] = "img/_withdraw_new.png"; // Withdraw
      path[4] = "img/_search_new.png"; 	 // Search
      path[5] = "img/_exit_new.png";	 // Exit
      
      button = new JButton[6];	// 메인 페이지 버튼 정의
      for (int i = 0; i < button.length; i++) {
    	  button[i] = new JButton();
    	  button[i].setIcon(new ImageIcon(getClass().getResource(path[i])));
    	  button[i].setDisabledIcon(icon);
    	  button[i].setBackground(Color.WHITE);
    	  button[i].setFocusable(false);		// Focus 안되게 설정
    	  
    	  if(i == 2 || i == 3) {
    		  button[i].setEnabled(false);	// 버튼 비활성화
    	  }
      }
      //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      
      // 회원가입 페이지 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      button_0_join = new JButton("회원가입");
      button_0_cancel = new JButton("취소");
      button_0_idCheck = new JButton("아이디 중복확인");
      //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      // 정보조회 페이지 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      button_4_cancel = new JButton("돌아가기");
      //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      // 로그인 페이지 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      button_1_ok = new JButton("로그인");
      button_1_cancel = new JButton("돌아가기");
      b_logout_ok = new JButton("로그아웃");
      //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      // 입금 페이지 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      button_2_ok = new JButton("입금하기");
      button_2_cancel = new JButton("취소");
      //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      // 출금 페이지 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      button_3_ok = new JButton("출금하기");
      button_3_cancel = new JButton("취소");
      //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      
/****************************************************************************************************************/
	      
      // MainPage ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      panel[0].setLayout(new GridLayout(3,2));
      //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      
      /* 2017-01-18 회원가입  RegisterPage 완료 */
      // RegisterPage ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      panel[1].setLayout(new FlowLayout()); panel_1_1 = new JPanel(); panel_1_1.setLayout(new GridLayout(9,3,5,10));
      panel_1_1.add(label[0]); panel_1_1.add(label[1]); panel_1_1.add(label[2]);   
      panel_1_1.add(label_id); panel_1_1.add(tField_id); panel_1_1.add(button_0_idCheck);
      panel_1_1.add(label_name); panel_1_1.add(tField_name); panel_1_1.add(label[3]);
      panel_1_1.add(label_pw); panel_1_1.add(pwTf); panel_1_1.add(label[4]);
      panel_1_1.add(label_pw2); panel_1_1.add(pwTf2); panel_1_1.add(label[5]);   
      panel_1_1.add(label_startbalance); panel_1_1.add(tField_balance); panel_1_1.add(label[6]);   
      panel_1_1.add(label[7]); panel_1_1.add(label[8]); panel_1_1.add(label[9]);   
      panel_1_1.add(label[10]); panel_1_1.add(label[11]); panel_1_1.add(label[12]);   
      panel_1_1.add(button_0_join); panel_1_1.add(label[13]); panel_1_1.add(button_0_cancel);
      // label[] endNum 13
      //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      
      // SearchPage ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      panel[5].setLayout(new FlowLayout()); panel_5_1 = new JPanel(); panel_5_1.setLayout(new BorderLayout());
      table.getTableHeader().setReorderingAllowed(false); // 테이블 이동 고정
      table.getTableHeader().setResizingAllowed(false); // 테이블 크기 고정
      table.setRowHeight(25); // 테이블 셀 높이 설정
      table.setShowHorizontalLines(true); // 칸 나눔선 표시
      sPanel = new JScrollPane(table);
      // Header 정렬 
      DefaultTableCellRenderer tHeader = (DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer();
      tHeader.setHorizontalAlignment(SwingConstants.CENTER);
      table.getTableHeader().setDefaultRenderer(tHeader);
      // Column 정렬
      DefaultTableCellRenderer tColumn = new DefaultTableCellRenderer();
      tColumn.setHorizontalAlignment(SwingConstants.CENTER);
      TableColumnModel tColumnModel = table.getColumnModel();
      // 테이블 내 모든 컬럼을 재배치
      for (int i = 0; i < tColumnModel.getColumnCount(); i++) {
    	  tColumnModel.getColumn(i).setCellRenderer(tColumn);
      }
      panel_5_1.add(sPanel, BorderLayout.CENTER);      
      panel_5_2 = new JPanel(); panel_5_2.setLayout(new GridLayout(1,3,5,10));
      panel_5_2.add(label[14]); panel_5_2.add(button_4_cancel); panel_5_2.add(label[15]);  // label[] endNum 15
      //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      
      // LoginPage ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      panel[2].setLayout(new FlowLayout());
      panel_2_1 = new JPanel();
      panel_2_1.setLayout(new GridLayout(5,2,5,10));
      panel_2_1.add(label[16]); panel_2_1.add(label[17]);
      panel_2_1.add(label_login_id); panel_2_1.add(tf_login_id);
      panel_2_1.add(label_login_pw); panel_2_1.add(pf_login_pw);   
      panel_2_1.add(label[18]); panel_2_1.add(label[19]);   
      panel_2_1.add(button_1_ok); panel_2_1.add(button_1_cancel);  // label[] endNum 19
      //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

      // DepositPage ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ    
      panel[3].setLayout(new FlowLayout());
      panel_3_1 = new JPanel();
      panel_3_1.setLayout(new GridLayout(3,4,5,10));
      panel_3_1.add(label[20]); panel_3_1.add(label[21]); panel_3_1.add(label[22]); panel_3_1.add(label[23]);
      panel_3_1.add(label[24]); panel_3_1.add(label_deposit_money);  panel_3_1.add(tf_deposit_money); panel_3_1.add(label[25]); 
      panel_3_1.add(label[26]); panel_3_1.add(label[27]);  panel_3_1.add(label[28]); panel_3_1.add(label[29]);
      panel_3_2 = new JPanel();
      panel_3_2.setLayout(new FlowLayout());
      panel_3_2.add(button_2_ok);
      panel_3_2.add(button_2_cancel);  // label[] endNum 29
      //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 
      
      // WithdrawPage ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ    
      panel[4].setLayout(new FlowLayout()); 
      panel_4_1 = new JPanel();
      panel_4_1.setLayout(new GridLayout(3,4,5,10)); 
      panel_4_1.add(label[30]); panel_4_1.add(label[31]); panel_4_1.add(label[32]); panel_4_1.add(label[33]);
      panel_4_1.add(label[34]); panel_4_1.add(label_withdraw_money); panel_4_1.add(tf_withdraw_money); panel_4_1.add(label[35]); 
      panel_4_1.add(label[36]); panel_4_1.add(label[37]); panel_4_1.add(label[38]); panel_4_1.add(label[39]);
      panel_4_2 = new JPanel(); 
      panel_4_2.setLayout(new FlowLayout());
      panel_4_2.add(button_3_ok);
      panel_4_2.add(button_3_cancel);  // label[] endNum 39
      //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

      // 각 Page의 SubPage 구성 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      panel[1].add(panel_1_1);
      panel[2].add(panel_2_1);
      panel[3].add(panel_3_1, "North");
      panel[3].add(panel_3_2, "Center");
      panel[4].add(panel_4_1, "North");
      panel[4].add(panel_4_2, "Center");
      panel[5].add(panel_5_1, "Center");
      panel[5].add(panel_5_2, "South");
      //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      
      // MainPage Button 구성 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      for(int i = 0; i < button.length; i++) {
    	  panel[0].add(button[i]);
      }
      //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      
      // CardLayout 구성 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      add(panel[0], "main");
      add(panel[1], "register");
      add(panel[2], "login");
      add(panel[3], "diposit");
      add(panel[4], "withdraw");
      add(panel[5], "search");
      //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      cards.show(this, "main");   // 프로그램 시작 시 첫 화면 설정
      
      // MainPage Button Add Action ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      for(int i = 0; i < button.length; i++) {
    	  button[i].addActionListener(this);
      }
      //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      
      /* SubPage Button ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ*/
      // RegisterPage Button Add Action ㅡㅡㅡㅡㅡ
      button_0_idCheck.addActionListener(this);
      button_0_join.addActionListener(this);
      button_0_cancel.addActionListener(this);
      
      // SearchPage Button Add Action ㅡㅡㅡㅡㅡㅡㅡ
      button_4_cancel.addActionListener(this);

      // LoginPage Button Add Action ㅡㅡㅡㅡㅡㅡㅡㅡ
      button_1_ok.addActionListener(this);
      button_1_cancel.addActionListener(this);
      b_logout_ok.addActionListener(this);

      // Deposit Check Button Add Action ㅡㅡㅡㅡ
      button_2_ok.addActionListener(this);
      button_2_cancel.addActionListener(this);

      // Withdraw Check Button Add Action
      button_3_ok.addActionListener(this);
      button_3_cancel.addActionListener(this);
      //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      setSize(350, 590);
      setMaximumSize(getSize());
      setVisible(true);      
   }
   // Java_Awt_BankSystem() End ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
   
   // 1. ThreadState Method	   
   public void loginState(boolean state){	// 로그인 시 쓰레드 일시정지 & 로그아웃 시 쓰레드 재개
	   synchronized(t1){
		   if(! state){
			   try{ setTitle("로그인된 아이디 : "+tf_login_id.getText()); t1.wait();
			   } catch(InterruptedException e){ e.printStackTrace(); }
		   }else{ t1.notify(); }
	   }
   }
   
   // 2. Button Action Method
   public void actionPerformed(ActionEvent e) {
      if (e.getSource() == button[0]) {	setSize(490, 400); setLocation(650, 300);
         							    button_0_join.setEnabled(false); cards.show(this, "register"); }	// Register View Button End
      else if (e.getSource() == button_0_idCheck) { checkId(); } // Register_ID_Check Button End
      else if (e.getSource() == button_0_join) { register(); } // Register_Join Button End
      else if (e.getSource() == button_0_cancel) { dialog[2].setVisible(true); setSize(350, 590); setLocation(700, 300);
         					 					   tField_id.setText(""); tField_name.setText(""); pwTf.setText(""); pwTf2.setText(""); 
         					 					   tField_balance.setText(""); cards.show(this, "main"); } // Register_Cancel Button End

      else if (e.getSource() == button[1]) { 
    	  // 로그인 : true / 로그아웃 : false
    	  if(typeChange == true){
	  		setSize(240, 250); setLocation(750, 350);
	  		cards.show(this, "login");
	  	  } else {
	  		  // 로그아웃으로 바꾸기 위한 변환
	  		  typeChange = true; loginState(typeChange); tf_login_id.setText(""); pf_login_pw.setText("");
	  		  button[1].setIcon(new ImageIcon(getClass().getResource("img/_login_new.png")));
			  button[2].setEnabled(false); button[3].setEnabled(false); 
			  setSize(350, 590); setLocation(700, 300);
			  cards.show(this, "main");
	      }
      } // Login View Button End
      else if (e.getSource() == button_1_ok) { login(); } // Login_OK Button End
	  else if (e.getSource() == button_1_cancel) { dialog[5].setVisible(true); setSize(350, 590); setLocation(700, 300);
	     										   cards.show(this, "main"); } // Login_Cancel Button End
      
	  else if (e.getSource() == button[2]) { setSize(300, 200); setLocation(700, 300); setTitle("입금 아이디 : "+tf_login_id.getText()); 
	  									 	 cards.show(this, "diposit"); } // Deposit View Button End
      else if (e.getSource() == button_2_ok) { deposit(); } // Deposit_Ok Button End
      else if (e.getSource() == button_2_cancel) { dialog[5].setVisible(true); tf_deposit_money.setText(""); setTitle("로그인된 아이디 : "+tf_login_id.getText());
      											   setSize(350, 590); setLocation(700, 300); cards.show(this, "main"); } // Deposit_Cancel Button End
      
      else if (e.getSource() == button[3]) { setSize(300, 200); setLocation(700, 300); setTitle("출금 아이디 : "+tf_login_id.getText()); 
      									     cards.show(this, "withdraw"); } // Withdraw View Button End
      else if (e.getSource() == button_3_ok) { withdraw(); } // Withdraw_Ok Button End
      else if (e.getSource() == button_3_cancel) { dialog[5].setVisible(true); tf_withdraw_money.setText(""); setTitle("로그인된 아이디 : "+tf_login_id.getText());
	     										   setSize(350, 590); setLocation(700, 300); cards.show(this, "main"); } // Withdraw_Cancel Button End

	  else if (e.getSource() == button[4]) { setSize(480, 520); setLocation(750, 350); accountList(); cards.show(this, "search"); } // Search View Button End
	  else if (e.getSource() == button_4_cancel) { dialog[3].setVisible(true); setSize(350, 590); setLocation(700, 300); cards.show(this, "main"); } // Search_Cancel Button End
      //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      else if (e.getSource() == button[5]) { // 프로그램 종료 시 TXT File로 회원정보 저장
    	 try {
    		 if(accountArray.size() != 0) {
    			 String saveName = ""; String saveId = ""; String savePw = ""; String saveBal ="";
    			 FileWriter fw = new FileWriter(new File("D:/Hangyu_Info.txt"));
    			 
    			 for(int i = 0; i < accountArray.size(); i++) {
    				 saveId = accountArray.get(i).getId();
    				 saveName = "|" + accountArray.get(i).getName();
    				 savePw = "|" + accountArray.get(i).getPassword();
    				 saveBal = "|" + accountArray.get(i).getBalance() + "|";
    				 fw.write(saveId+saveName+savePw+saveBal);	// ID, NAME, PASSWORD, BALANCE 순으로 저장
    			 }
    			 fw.close();
    		 } 
    		 System.exit(0); // 파일 저장 후에 프로그램 종료
    	 } catch (IOException e1) { e1.printStackTrace(); }  
      } // Exit View Button End 
   }
   // actionPerformed(ActionEvent e) End ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
   
   // 3. Register Method
   public void register() {
	   // 입력 예외처리  ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ   // 회원가입 예외처리 완료
	   String id = tField_id.getText().trim(); // 아이디 공백 체크는 아이디 중복확인 메소드 : checkId 에서 실행
	   String name = tField_name.getText().trim();
	   if(name.equals("")){ dialog[12].setVisible(true); return; } // 이름 공백 체크 
	   String pw = pwTf.getText().trim(); // passwordField에서는 .getText()를 사용 할 수는 있지만 권장하진 않는다.
	   if(pw.equals("")) { dialog[13].setVisible(true); return; } // 암호 공백 체크 	
	   String pw2 = pwTf2.getText().trim();
	   if(pw2.equals("")) { dialog[14].setVisible(true); return; } // 암호 재확인 공백 체크 
	   String b = tField_balance.getText().trim(); 
	   if (b.equals("")) { dialog[15].setVisible(true); return; } // 예금액 공백 체크
	   int balance = Integer.parseInt(b);
	   // 입력 예외처리 End ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
      
	   // Password Check  ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	   if(pw.equals(pw2)) {
		   Bank_Account newAccount = new Bank_Account(id, name, pw, balance);
		   accountArray.add(newAccount);
		   dialog[1].setVisible(true); 
		   setSize(350, 590); setLocation(700, 300);
		   tField_id.setText(""); tField_name.setText(""); pwTf.setText(""); pwTf2.setText(""); tField_balance.setText("");
		   cards.show(this, "main");
	   } else { dialog[11].setVisible(true); pwTf2.setText(""); return; }
      // Password Check End ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
   }
   // register() End ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
   
   // 4. Register Id Check Method - 미리 가입 된 아이디와 생성하는 아이디 중복 체크
   public Bank_Account checkMember(String id) { 
      Bank_Account oldAccount = null; 
      for(int i = 0; i < accountArray.size(); i++) { 
    	  String dbId = accountArray.get(i).getId();
    	  if(dbId.equals(id)){ oldAccount = accountArray.get(i); break; }
      }
      return oldAccount;
   }
   // 미리 가입 된 아이디와 생성하는 아이디 중복 체크 End ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

   // 5. Register Id Button Overlap Clicked Method - 아이디 중복 체크 버튼 클릭 시
   public void checkId() { 
      String id = tField_id.getText().trim();
      if(!id.equals(tField_id.getText())) {	dialog[0].setVisible(true); tField_id.setText(""); return; } // 아이디 공백 체크 
      Bank_Account account = checkMember(id);
      if(id.equals("")){ dialog[10].setVisible(true); return; } // 아이디가 공백
      if(account != null) { dialog[9].setVisible(true); tField_id.setText(""); return; // 아이디가 중복
      } else { dialog[8].setVisible(true); button_0_join.setEnabled(true); return; } // 아이디 사용가능
   }
   // 아이디 중복 체크 버튼 클릭시  End ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

   // 6. Login Method - 01_24 로그인 구현 테스트 완료
   public void login() {
		   if(tf_login_id.getText().equals("")) { dialog[10].setVisible(true); return;
		   } else if(pf_login_pw.getText().equals("")) { dialog[13].setVisible(true); return;
		   } else {
			   String eId = tf_login_id.getText(); String ePassword = pf_login_pw.getText(); 
			   Bank_Account checkInfo = loginAuth(eId, ePassword);
			   if(checkInfo != null) { dialog[4].setVisible(true);
			   typeChange = false; // 로그아웃으로 바꾸기 위한 변환
			   button[1].setIcon(new ImageIcon(getClass().getResource("img/_logout_new.png")));
			   button[2].setEnabled(true); button[3].setEnabled(true);
			   setSize(350, 590); setLocation(700, 300);
			   
			   cards.show(this, "main"); } else { tf_login_id.setText(""); pf_login_pw.setText(""); } // 정보가 없으면 필드 초기화
		   }
   }
   // login() End ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 
  
   // 7. Login Auth Button Clicked Method - 01_24 사용자인증 테스트 완료 
   public Bank_Account loginAuth(String id, String password) {
	  Bank_Account checkInfo = null; 
	  if(accountArray.size() == 0) { dialog[6].setVisible(true); tf_login_id.setText(""); pf_login_pw.setText(""); return checkInfo; }
	  int count = 0;
	  for(int i = 0; i < accountArray.size(); i++) { String dbId = accountArray.get(i).getId(); 
	  String dbPw = accountArray.get(i).getPassword();
		  if(dbId.equals(id)) {
				if(dbPw.equals(password)) { checkInfo = accountArray.get(i); break;	   
				} else { dialog[11].setVisible(true); pf_login_pw.setText(""); break; 
				}
		  }count++;
	  }
	  if(accountArray.size() == count) { dialog[7].setVisible(true); tf_login_id.setText(""); }
	  return checkInfo;
   }
   // 사용자 인증 End ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
   
   // 8. Search Method - 01_24 정보 조회 구현 완료
   private void accountList() {
	   Vector<String> Row;
	   model.getDataVector().clear(); // 테이블 초기화
	   for(int i = 0; i < accountArray.size(); i++) {   
		   String Id = accountArray.get(i).getId(); String Name = accountArray.get(i).getName();
		   long Balance = accountArray.get(i).getBalance(); String B = String.valueOf(Balance);
		   Row = new Vector<String>(); Row.addElement(Id); Row.addElement(Name); Row.addElement(B); model.addRow(Row);      
	   }
   }
   // 정보 테이블 조회 End ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

   // 9. Deposit Method
   public void deposit () {
	   if(tf_deposit_money.getText().equals("")) { dialog[16].setVisible(true); return; }  // 입금 공백 
	   String id = tf_login_id.getText(); String password = pf_login_pw.getText();
	   Bank_Account depositAuth = loginAuth(id, password);
	   
	   if(depositAuth != null) {
		   long addBalance = (long) Integer.parseInt(tf_deposit_money.getText());
		   depositAuth.addBalance(addBalance);
		   dialog[19].setVisible(true); // 입금 완료 
		   tf_deposit_money.setText("");
		   setTitle("로그인된 아이디 : "+tf_login_id.getText());
		   setSize(350, 590); setLocation(700, 300);
		   cards.show(this, "main");
	   }
   }
   // 입금 End ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

   // 10. 입,출금 시 사용자 정보 가져오기 Method
   public Bank_Account checkDeposit (String id, String password) {
	   Bank_Account depositUserInfo = null; 
	   for(int i = 0; i < accountArray.size(); i++) { 
		  String dbId = accountArray.get(i).getId(); String dbPw = accountArray.get(i).getPassword();
		  if(dbId.equals(id)) {
			  if(dbPw.equals(password)) { depositUserInfo = accountArray.get(i); break; }
		  }
	   }
	   return depositUserInfo;
   }
   // 입,출금 시 사용자 정보 가져오기 End ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
   
   // 11. Withdraw Method
   public void withdraw() {
	   if(tf_withdraw_money.getText().equals("")) {
		   dialog[17].setVisible(true); // 출금 공백
		   return;
	   }
	   String id = tf_login_id.getText();
	   String password = pf_login_pw.getText();
	   Bank_Account withdrawAuth = loginAuth(id, password);
	   
	   if(withdrawAuth != null) {
		   long oldWithdrawBalance = withdrawAuth.getBalance();
		   long withdrawBalance = (long) Integer.parseInt(tf_withdraw_money.getText());
		   
		   if(oldWithdrawBalance < withdrawBalance) {
			   dialog[18].setVisible(true); // 잔액 부족
			   tf_withdraw_money.setText("");
			   return;
		   } else {
			   withdrawAuth.withdrawBalance(withdrawBalance);
			   dialog[19].setVisible(true); // 출금 완료
			   tf_withdraw_money.setText("");
			   setSize(350, 590); setLocation(700, 300);
			   cards.show(this, "main");
		   }  
	   }
   }
   // 출금 End ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
  
   // 12. 타이틀 쓰레드 Method
   public void titleMove() throws InterruptedException {
		for(int i = 0; i < 17; i++) {	
			if(! typeChange){
				   loginState(typeChange); 
			}
			if(i == 0) { setTitle("B"); } 
			else if (i == 1) { Thread.sleep(250); setTitle("Ba"); } 
			else if (i == 2) { Thread.sleep(250); setTitle("Ban"); } 
			else if (i == 3) { Thread.sleep(250); setTitle("Bank"); } 
			else if (i == 4) { Thread.sleep(250); setTitle("Bank S"); } 
			else if (i == 5) { Thread.sleep(250); setTitle("Bank Sy"); }
			else if (i == 6) { Thread.sleep(250); setTitle("Bank Sys"); } 
			else if (i == 7) { Thread.sleep(250); setTitle("Bank Syst"); }
			else if (i == 8) { Thread.sleep(250); setTitle("Bank Syste"); } 
			else if (i == 9) { Thread.sleep(250); setTitle("Bank System"); } 
			else if (i == 10) { Thread.sleep(250); setTitle(""); } 
			else if (i == 11) { Thread.sleep(250); setTitle("Bank System"); } 
			else if (i == 12) { Thread.sleep(250); setTitle(""); } 
			else if (i == 13) { Thread.sleep(250); setTitle("Bank System"); } 
			else if (i == 14) { Thread.sleep(250); setTitle(""); } 
			else if (i == 15) { Thread.sleep(250); setTitle("Bank System"); } 
			else if (i == 16) { Thread.sleep(250); setTitle(""); }
		}
	}
   @Override
   public void run() {
	   while (true) {
		   try { 
				titleMove();
				Thread.sleep(250);
			} catch (InterruptedException e) {
			e.printStackTrace();
			}
	   }
	}
   
   // 13. Main Method
   public static void main(String[] args) throws IOException { new Java_Awt_BankSystem(); }
}
class MyDialog extends JDialog {
	public MyDialog(Frame frame) { super(frame); }
}
