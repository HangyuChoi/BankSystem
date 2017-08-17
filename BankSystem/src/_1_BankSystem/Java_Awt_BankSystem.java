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

// BankSystem - by. �� �ѱ�

public class Java_Awt_BankSystem extends Frame implements ActionListener, Runnable{
	boolean typeChange; // �α��� <-> �α׾ƿ� ��ȯ
	private List<Bank_Account> accountArray;   // 2017-01-23 modify Array �� ArrayList 
	public JButton[] button;	// ���� ��ư
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
   public JLabel[] label; // ���� �� �迭ȭ
   public JLabel label_id, label_name, label_pw, label_pw2, label_startbalance, label_search, label_login_id, 
   				 label_login_pw, label_deposit_money, label_withdraw_money;
   private JTextField tField_id, tField_name, tField_balance, tf_login_id, tf_deposit_money, tf_withdraw_money;
   public ImageIcon icon;
   public CardLayout cards;
   public Dialog[] dialog; // Dialog �ݺ� ���� �迭ȭ
   
   public String[] path; // ���� ��ư �̹��� ����ּ� ����
   public JPasswordField pwTf, pwTf2, pf_login_pw;
   public JTable table;
   public DefaultTableModel model;
   public JScrollPane sPanel;
   Thread t1;

   // �޼ҵ� line ǥ�� ########################################################
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
   	10. ��,��� �� ����� ���� �������� Method - checkDeposit  line : 619
   	11. Withdraw Method - withdraw  line : 632
   	12. Ÿ��Ʋ ������ Method - titleMove  line : 661
   	13. Main Method - main  line : 698
   	  
   */
   //#####################################################################
   
   // Java_Awt_BankSystem() 
   public Java_Awt_BankSystem() throws IOException {
	  t1 = new Thread(this);
	  t1.start();	// ������ ���� ( Ÿ��Ʋ �׼� )
	  typeChange = true;

      accountArray = new ArrayList<Bank_Account>();   // 2017-01-23 modify Array �� ArrayList  
      
      // ȸ������ �������� ������ ������ FileNotFoundException , ȸ������ �ҷ����⸦ �ǳʶٰ� ���α׷� ����
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
		 
	  // ������ ȸ�������� accountArray�� ���
	  while(st.hasMoreTokens()) {
		  addId = st.nextToken();
		  System.out.println("���̵� : "+ addId + " ���� ���� ���");
		  addName = st.nextToken();
		  addPw = st.nextToken();
		  addBal = st.nextToken();
		  addBal2 =(long) Integer.parseInt(addBal);
		  Bank_Account newAccount = new Bank_Account(addId, addName, addPw, addBal2);
		  accountArray.add(newAccount);
	  } 	

      Vector<String> Column = new Vector<String>();	// �÷��� List - Vector ���
      // �� �߰� 
	  Column.addElement("���̵�");	Column.addElement("�̸�"); Column.addElement("�ܾ�");
		 
	  model = new DefaultTableModel(Column, 0){ // ���̺� �� ���� 
		  public boolean isCellEditable(int row, int column){ // ���� ����
 		     return false;
 		  }
	  };
      table = new JTable(model); // ���̺� ����
      cards = new CardLayout();
      setLayout(cards);
      setLocation(700, 300);

      // CardLayout View Panel ���� �ѤѤѤѤѤѤ�
      
      panel = new JPanel[6];
      for(int i = 0; i < panel.length; i++) {
    	  panel[i] = new JPanel();
      }
      panel[0] = new JPanel();	/*���� */
      panel[1] = new JPanel();	/*ȸ������*/
      panel[2] = new JPanel();	/*�α��� */
      panel[3] = new JPanel();  /*�Ա� */
      panel[4] = new JPanel();	/*��� */
      panel[5] = new JPanel();	/*��ȸ */
      //�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�

      // Label[] ���� ���� �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      label = new JLabel[40];
      for (int i = 0; i < label.length; i++) { label[i] = new JLabel(" "); }
      //�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
       
      /* Dialog ���� */		/* Dialog �ݺ��� ���� */ 
      // 2017-02-06 ���� �Ϸ�
      dialog = new Dialog[25];
      for (int i = 0; i < dialog.length; i++) { dialog[i] = new MyDialog(this); dialog[i].setLayout(new FlowLayout()); dialog[i].setLocation(800, 500); }
      // ���� üũ - Dialog �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      dialog[0].add(new JLabel("������ �ȵ˴ϴ�."));
      // ȸ������ �Ϸ� - Dialog �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      dialog[1].add(new JLabel("ȸ�������� �Ϸ� �Ǿ����ϴ�."));
      // ȸ������ ��� - Dialog �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      dialog[2].add(new JLabel("ȸ�������� ��� �Ǿ����ϴ�."));
      // ������ȸ ��� - Dialog �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      dialog[3].add(new JLabel("��ȸ�� �����մϴ�."));
      // �α��� �Ϸ� - Dialog �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      dialog[4].add(new JLabel("�α��� �Ǽ̽��ϴ�."));
      // �α��� ��� - Dialog �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      dialog[5].add(new JLabel("����ȭ������ ���ư��ϴ�."));
      // ȸ���� �ƹ��� ���� �� �α��� - Dialog �ѤѤѤѤѤѤѤѤѤѤѤ�
      dialog[6].add(new JLabel("ȸ�������� ���ּ���."));
      // ���̵� Ʋ�� - Dialog �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      dialog[7].add(new JLabel("���̵� �������� �ʽ��ϴ�."));
      // ���̵� ��밡�� - Dialog �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      dialog[8].add(new JLabel("��밡�� �� ���̵� �Դϴ�."));
      // ���̵� �ߺ� - Dialog �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      dialog[9].add(new JLabel("���̵� �ߺ� �˴ϴ�."));
      // ���̵� �� ���� - Dialog �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      dialog[10].add(new JLabel("���̵� �Է��ϼ���."));
      // ��й�ȣ üũ - Dialog �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      dialog[11].add(new JLabel("��й�ȣ�� ���� �ʽ��ϴ�. �ٽ� �Է����ּ���."));
      // �̸� ������ ��  - Dialog �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      dialog[12].add(new JLabel("�̸��� �Է� ���ּ���."));
      // ��ȣ ������ ��  - Dialog �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      dialog[13].add(new JLabel("��й�ȣ�� �Է� ���ּ���."));
      // ��ȣ ��Ȯ���� ������ ��  - Dialog �ѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      dialog[14].add(new JLabel("��й�ȣ�� �ѹ� �� �Է����ּ���."));
      // ��ü������ ������ ��  - Dialog �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      dialog[15].add(new JLabel("��� ������ �Է� ���ּ���."));
      // �Ա� �ݾ��� ������ �� - Dialog �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      dialog[16].add(new JLabel("�Ա��Ͻ� �ݾ��� �Է� ���ּ���."));
  	  // ��� �ݾ��� ������ �� - Dialog �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      dialog[17].add(new JLabel("����Ͻ� �ݾ��� �Է� ���ּ���."));
      // �ܾ� ���� - Dialog �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�   
      dialog[18].add(new JLabel("�ܾ��� �����մϴ�. �ٽ� �Է����ּ���."));
      // �Ա� & ��� ���� - Dialog �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      dialog[19].add(new JLabel("�̿��� �ּż� �����մϴ�."));
      
      for (int i = 0; i < dialog.length; i++) {	dialog[i].pack(); } // ������� ��ŭ ����� �ø�
      //�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
       
      /* Label, TextField ����  */                                    
      // ȸ������ ������  �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      label_id = new JLabel("���̵�"); tField_id = new JTextField(); 
      label_name = new JLabel("�̸�"); tField_name = new JTextField();
      label_pw = new JLabel("��й�ȣ "); pwTf = new JPasswordField();
      label_pw2 = new JLabel("��й�ȣ ��Ȯ�� "); pwTf2 = new JPasswordField();
      label_startbalance = new JLabel("�ʱ� ���ݾ� "); tField_balance = new JTextField();
      //�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      // �α��� ������ �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      label_login_id = new JLabel("���̵�"); tf_login_id = new JTextField();
      label_login_pw = new JLabel("��й�ȣ"); pf_login_pw = new JPasswordField();
      //�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      // �Ա� ������  �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      label_deposit_money = new JLabel("�Ա��Ͻ� �ݾ� "); tf_deposit_money = new JTextField();
      //�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      // ��� ������ �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      label_withdraw_money = new JLabel("����Ͻ� �ݾ� "); tf_withdraw_money = new JTextField();
      //�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      
      
      /* Button ���� */ 
      // ���� ������ �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      
      path = new String[6];	// ��ư �̹��� ����ּ�
      path[0] = "img/_register_new.png"; // Register
      path[1] = "img/_login_new.png";	 // Login
      path[2] = "img/_deposit_new.png";	 // Deposit
      path[3] = "img/_withdraw_new.png"; // Withdraw
      path[4] = "img/_search_new.png"; 	 // Search
      path[5] = "img/_exit_new.png";	 // Exit
      
      button = new JButton[6];	// ���� ������ ��ư ����
      for (int i = 0; i < button.length; i++) {
    	  button[i] = new JButton();
    	  button[i].setIcon(new ImageIcon(getClass().getResource(path[i])));
    	  button[i].setDisabledIcon(icon);
    	  button[i].setBackground(Color.WHITE);
    	  button[i].setFocusable(false);		// Focus �ȵǰ� ����
    	  
    	  if(i == 2 || i == 3) {
    		  button[i].setEnabled(false);	// ��ư ��Ȱ��ȭ
    	  }
      }
      //�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      
      // ȸ������ ������ �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      button_0_join = new JButton("ȸ������");
      button_0_cancel = new JButton("���");
      button_0_idCheck = new JButton("���̵� �ߺ�Ȯ��");
      //�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      // ������ȸ ������ �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      button_4_cancel = new JButton("���ư���");
      //�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      // �α��� ������ �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      button_1_ok = new JButton("�α���");
      button_1_cancel = new JButton("���ư���");
      b_logout_ok = new JButton("�α׾ƿ�");
      //�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      // �Ա� ������ �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      button_2_ok = new JButton("�Ա��ϱ�");
      button_2_cancel = new JButton("���");
      //�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      // ��� ������ �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      button_3_ok = new JButton("����ϱ�");
      button_3_cancel = new JButton("���");
      //�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      
/****************************************************************************************************************/
	      
      // MainPage �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      panel[0].setLayout(new GridLayout(3,2));
      //�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      
      /* 2017-01-18 ȸ������  RegisterPage �Ϸ� */
      // RegisterPage �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
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
      //�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      
      // SearchPage �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      panel[5].setLayout(new FlowLayout()); panel_5_1 = new JPanel(); panel_5_1.setLayout(new BorderLayout());
      table.getTableHeader().setReorderingAllowed(false); // ���̺� �̵� ����
      table.getTableHeader().setResizingAllowed(false); // ���̺� ũ�� ����
      table.setRowHeight(25); // ���̺� �� ���� ����
      table.setShowHorizontalLines(true); // ĭ ������ ǥ��
      sPanel = new JScrollPane(table);
      // Header ���� 
      DefaultTableCellRenderer tHeader = (DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer();
      tHeader.setHorizontalAlignment(SwingConstants.CENTER);
      table.getTableHeader().setDefaultRenderer(tHeader);
      // Column ����
      DefaultTableCellRenderer tColumn = new DefaultTableCellRenderer();
      tColumn.setHorizontalAlignment(SwingConstants.CENTER);
      TableColumnModel tColumnModel = table.getColumnModel();
      // ���̺� �� ��� �÷��� ���ġ
      for (int i = 0; i < tColumnModel.getColumnCount(); i++) {
    	  tColumnModel.getColumn(i).setCellRenderer(tColumn);
      }
      panel_5_1.add(sPanel, BorderLayout.CENTER);      
      panel_5_2 = new JPanel(); panel_5_2.setLayout(new GridLayout(1,3,5,10));
      panel_5_2.add(label[14]); panel_5_2.add(button_4_cancel); panel_5_2.add(label[15]);  // label[] endNum 15
      //�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      
      // LoginPage �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      panel[2].setLayout(new FlowLayout());
      panel_2_1 = new JPanel();
      panel_2_1.setLayout(new GridLayout(5,2,5,10));
      panel_2_1.add(label[16]); panel_2_1.add(label[17]);
      panel_2_1.add(label_login_id); panel_2_1.add(tf_login_id);
      panel_2_1.add(label_login_pw); panel_2_1.add(pf_login_pw);   
      panel_2_1.add(label[18]); panel_2_1.add(label[19]);   
      panel_2_1.add(button_1_ok); panel_2_1.add(button_1_cancel);  // label[] endNum 19
      //�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�

      // DepositPage �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�    
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
      //�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ� 
      
      // WithdrawPage �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�    
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
      //�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�

      // �� Page�� SubPage ���� �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      panel[1].add(panel_1_1);
      panel[2].add(panel_2_1);
      panel[3].add(panel_3_1, "North");
      panel[3].add(panel_3_2, "Center");
      panel[4].add(panel_4_1, "North");
      panel[4].add(panel_4_2, "Center");
      panel[5].add(panel_5_1, "Center");
      panel[5].add(panel_5_2, "South");
      //�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      
      // MainPage Button ���� �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      for(int i = 0; i < button.length; i++) {
    	  panel[0].add(button[i]);
      }
      //�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      
      // CardLayout ���� �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      add(panel[0], "main");
      add(panel[1], "register");
      add(panel[2], "login");
      add(panel[3], "diposit");
      add(panel[4], "withdraw");
      add(panel[5], "search");
      //�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      cards.show(this, "main");   // ���α׷� ���� �� ù ȭ�� ����
      
      // MainPage Button Add Action �ѤѤѤѤѤѤѤѤ�
      for(int i = 0; i < button.length; i++) {
    	  button[i].addActionListener(this);
      }
      //�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      
      /* SubPage Button �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�*/
      // RegisterPage Button Add Action �ѤѤѤѤ�
      button_0_idCheck.addActionListener(this);
      button_0_join.addActionListener(this);
      button_0_cancel.addActionListener(this);
      
      // SearchPage Button Add Action �ѤѤѤѤѤѤ�
      button_4_cancel.addActionListener(this);

      // LoginPage Button Add Action �ѤѤѤѤѤѤѤ�
      button_1_ok.addActionListener(this);
      button_1_cancel.addActionListener(this);
      b_logout_ok.addActionListener(this);

      // Deposit Check Button Add Action �ѤѤѤ�
      button_2_ok.addActionListener(this);
      button_2_cancel.addActionListener(this);

      // Withdraw Check Button Add Action
      button_3_ok.addActionListener(this);
      button_3_cancel.addActionListener(this);
      //�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      setSize(350, 590);
      setMaximumSize(getSize());
      setVisible(true);      
   }
   // Java_Awt_BankSystem() End �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
   
   // 1. ThreadState Method	   
   public void loginState(boolean state){	// �α��� �� ������ �Ͻ����� & �α׾ƿ� �� ������ �簳
	   synchronized(t1){
		   if(! state){
			   try{ setTitle("�α��ε� ���̵� : "+tf_login_id.getText()); t1.wait();
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
    	  // �α��� : true / �α׾ƿ� : false
    	  if(typeChange == true){
	  		setSize(240, 250); setLocation(750, 350);
	  		cards.show(this, "login");
	  	  } else {
	  		  // �α׾ƿ����� �ٲٱ� ���� ��ȯ
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
      
	  else if (e.getSource() == button[2]) { setSize(300, 200); setLocation(700, 300); setTitle("�Ա� ���̵� : "+tf_login_id.getText()); 
	  									 	 cards.show(this, "diposit"); } // Deposit View Button End
      else if (e.getSource() == button_2_ok) { deposit(); } // Deposit_Ok Button End
      else if (e.getSource() == button_2_cancel) { dialog[5].setVisible(true); tf_deposit_money.setText(""); setTitle("�α��ε� ���̵� : "+tf_login_id.getText());
      											   setSize(350, 590); setLocation(700, 300); cards.show(this, "main"); } // Deposit_Cancel Button End
      
      else if (e.getSource() == button[3]) { setSize(300, 200); setLocation(700, 300); setTitle("��� ���̵� : "+tf_login_id.getText()); 
      									     cards.show(this, "withdraw"); } // Withdraw View Button End
      else if (e.getSource() == button_3_ok) { withdraw(); } // Withdraw_Ok Button End
      else if (e.getSource() == button_3_cancel) { dialog[5].setVisible(true); tf_withdraw_money.setText(""); setTitle("�α��ε� ���̵� : "+tf_login_id.getText());
	     										   setSize(350, 590); setLocation(700, 300); cards.show(this, "main"); } // Withdraw_Cancel Button End

	  else if (e.getSource() == button[4]) { setSize(480, 520); setLocation(750, 350); accountList(); cards.show(this, "search"); } // Search View Button End
	  else if (e.getSource() == button_4_cancel) { dialog[3].setVisible(true); setSize(350, 590); setLocation(700, 300); cards.show(this, "main"); } // Search_Cancel Button End
      //�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      else if (e.getSource() == button[5]) { // ���α׷� ���� �� TXT File�� ȸ������ ����
    	 try {
    		 if(accountArray.size() != 0) {
    			 String saveName = ""; String saveId = ""; String savePw = ""; String saveBal ="";
    			 FileWriter fw = new FileWriter(new File("D:/Hangyu_Info.txt"));
    			 
    			 for(int i = 0; i < accountArray.size(); i++) {
    				 saveId = accountArray.get(i).getId();
    				 saveName = "|" + accountArray.get(i).getName();
    				 savePw = "|" + accountArray.get(i).getPassword();
    				 saveBal = "|" + accountArray.get(i).getBalance() + "|";
    				 fw.write(saveId+saveName+savePw+saveBal);	// ID, NAME, PASSWORD, BALANCE ������ ����
    			 }
    			 fw.close();
    		 } 
    		 System.exit(0); // ���� ���� �Ŀ� ���α׷� ����
    	 } catch (IOException e1) { e1.printStackTrace(); }  
      } // Exit View Button End 
   }
   // actionPerformed(ActionEvent e) End �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
   
   // 3. Register Method
   public void register() {
	   // �Է� ����ó��  �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�   // ȸ������ ����ó�� �Ϸ�
	   String id = tField_id.getText().trim(); // ���̵� ���� üũ�� ���̵� �ߺ�Ȯ�� �޼ҵ� : checkId ���� ����
	   String name = tField_name.getText().trim();
	   if(name.equals("")){ dialog[12].setVisible(true); return; } // �̸� ���� üũ 
	   String pw = pwTf.getText().trim(); // passwordField������ .getText()�� ��� �� ���� ������ �������� �ʴ´�.
	   if(pw.equals("")) { dialog[13].setVisible(true); return; } // ��ȣ ���� üũ 	
	   String pw2 = pwTf2.getText().trim();
	   if(pw2.equals("")) { dialog[14].setVisible(true); return; } // ��ȣ ��Ȯ�� ���� üũ 
	   String b = tField_balance.getText().trim(); 
	   if (b.equals("")) { dialog[15].setVisible(true); return; } // ���ݾ� ���� üũ
	   int balance = Integer.parseInt(b);
	   // �Է� ����ó�� End �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
      
	   // Password Check  �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
	   if(pw.equals(pw2)) {
		   Bank_Account newAccount = new Bank_Account(id, name, pw, balance);
		   accountArray.add(newAccount);
		   dialog[1].setVisible(true); 
		   setSize(350, 590); setLocation(700, 300);
		   tField_id.setText(""); tField_name.setText(""); pwTf.setText(""); pwTf2.setText(""); tField_balance.setText("");
		   cards.show(this, "main");
	   } else { dialog[11].setVisible(true); pwTf2.setText(""); return; }
      // Password Check End �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
   }
   // register() End �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
   
   // 4. Register Id Check Method - �̸� ���� �� ���̵�� �����ϴ� ���̵� �ߺ� üũ
   public Bank_Account checkMember(String id) { 
      Bank_Account oldAccount = null; 
      for(int i = 0; i < accountArray.size(); i++) { 
    	  String dbId = accountArray.get(i).getId();
    	  if(dbId.equals(id)){ oldAccount = accountArray.get(i); break; }
      }
      return oldAccount;
   }
   // �̸� ���� �� ���̵�� �����ϴ� ���̵� �ߺ� üũ End �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�

   // 5. Register Id Button Overlap Clicked Method - ���̵� �ߺ� üũ ��ư Ŭ�� ��
   public void checkId() { 
      String id = tField_id.getText().trim();
      if(!id.equals(tField_id.getText())) {	dialog[0].setVisible(true); tField_id.setText(""); return; } // ���̵� ���� üũ 
      Bank_Account account = checkMember(id);
      if(id.equals("")){ dialog[10].setVisible(true); return; } // ���̵� ����
      if(account != null) { dialog[9].setVisible(true); tField_id.setText(""); return; // ���̵� �ߺ�
      } else { dialog[8].setVisible(true); button_0_join.setEnabled(true); return; } // ���̵� ��밡��
   }
   // ���̵� �ߺ� üũ ��ư Ŭ����  End �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�

   // 6. Login Method - 01_24 �α��� ���� �׽�Ʈ �Ϸ�
   public void login() {
		   if(tf_login_id.getText().equals("")) { dialog[10].setVisible(true); return;
		   } else if(pf_login_pw.getText().equals("")) { dialog[13].setVisible(true); return;
		   } else {
			   String eId = tf_login_id.getText(); String ePassword = pf_login_pw.getText(); 
			   Bank_Account checkInfo = loginAuth(eId, ePassword);
			   if(checkInfo != null) { dialog[4].setVisible(true);
			   typeChange = false; // �α׾ƿ����� �ٲٱ� ���� ��ȯ
			   button[1].setIcon(new ImageIcon(getClass().getResource("img/_logout_new.png")));
			   button[2].setEnabled(true); button[3].setEnabled(true);
			   setSize(350, 590); setLocation(700, 300);
			   
			   cards.show(this, "main"); } else { tf_login_id.setText(""); pf_login_pw.setText(""); } // ������ ������ �ʵ� �ʱ�ȭ
		   }
   }
   // login() End �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ� 
  
   // 7. Login Auth Button Clicked Method - 01_24 ��������� �׽�Ʈ �Ϸ� 
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
   // ����� ���� End �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
   
   // 8. Search Method - 01_24 ���� ��ȸ ���� �Ϸ�
   private void accountList() {
	   Vector<String> Row;
	   model.getDataVector().clear(); // ���̺� �ʱ�ȭ
	   for(int i = 0; i < accountArray.size(); i++) {   
		   String Id = accountArray.get(i).getId(); String Name = accountArray.get(i).getName();
		   long Balance = accountArray.get(i).getBalance(); String B = String.valueOf(Balance);
		   Row = new Vector<String>(); Row.addElement(Id); Row.addElement(Name); Row.addElement(B); model.addRow(Row);      
	   }
   }
   // ���� ���̺� ��ȸ End �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�

   // 9. Deposit Method
   public void deposit () {
	   if(tf_deposit_money.getText().equals("")) { dialog[16].setVisible(true); return; }  // �Ա� ���� 
	   String id = tf_login_id.getText(); String password = pf_login_pw.getText();
	   Bank_Account depositAuth = loginAuth(id, password);
	   
	   if(depositAuth != null) {
		   long addBalance = (long) Integer.parseInt(tf_deposit_money.getText());
		   depositAuth.addBalance(addBalance);
		   dialog[19].setVisible(true); // �Ա� �Ϸ� 
		   tf_deposit_money.setText("");
		   setTitle("�α��ε� ���̵� : "+tf_login_id.getText());
		   setSize(350, 590); setLocation(700, 300);
		   cards.show(this, "main");
	   }
   }
   // �Ա� End �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�

   // 10. ��,��� �� ����� ���� �������� Method
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
   // ��,��� �� ����� ���� �������� End �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
   
   // 11. Withdraw Method
   public void withdraw() {
	   if(tf_withdraw_money.getText().equals("")) {
		   dialog[17].setVisible(true); // ��� ����
		   return;
	   }
	   String id = tf_login_id.getText();
	   String password = pf_login_pw.getText();
	   Bank_Account withdrawAuth = loginAuth(id, password);
	   
	   if(withdrawAuth != null) {
		   long oldWithdrawBalance = withdrawAuth.getBalance();
		   long withdrawBalance = (long) Integer.parseInt(tf_withdraw_money.getText());
		   
		   if(oldWithdrawBalance < withdrawBalance) {
			   dialog[18].setVisible(true); // �ܾ� ����
			   tf_withdraw_money.setText("");
			   return;
		   } else {
			   withdrawAuth.withdrawBalance(withdrawBalance);
			   dialog[19].setVisible(true); // ��� �Ϸ�
			   tf_withdraw_money.setText("");
			   setSize(350, 590); setLocation(700, 300);
			   cards.show(this, "main");
		   }  
	   }
   }
   // ��� End �ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�
  
   // 12. Ÿ��Ʋ ������ Method
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
