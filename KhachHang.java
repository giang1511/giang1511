package View;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Connection.ConnectJDBC;
import SignUp_SignIn.Infor;
import SignUp_SignIn.*;
import SignUp_SignIn.quenMK;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.imageio.ImageIO;
import javax.print.attribute.standard.JobOriginatingUserName;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout.Group;
import javax.swing.ImageIcon;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.SoftBevelBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.Component;
import javax.swing.SwingConstants;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import javax.swing.JRadioButton;

public class KhachHang extends JFrame implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;
	public JPanel contentPane;

	public JTextField Search_MaKH;
	public JTable table;
	public JTextField textField_makh;
	public JTextField textField_tenkh;
	public JTextField textField_tuoi;
	public JTextField textField_diachi;
	public JTextField textField_gioiTinh;
	public Component frame;

	public JLabel lblNote;
	public JLabel note;
	public JMenu dataTable;
	public JRadioButton nam;
	public JRadioButton nu;

	Vector<Vector<String>> vData = new Vector<Vector<String>>();
	Vector<String> vTitle = new Vector<String>();
	JScrollPane tableResult;
	DefaultTableModel Model;
	int selectedrow = 0;

	Statement stm;
	ResultSet rst;
	ConnectJDBC jdbc = new ConnectJDBC();
	Connection conn = null;

	public KhachHang() {

		try {

			conn = ConnectJDBC.getConnection();

			stm = conn.createStatement();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		setFont(new Font("Dialog", Font.ITALIC, 18));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 923, 581);
		setTitle("Khách Hàng");
		setForeground(Color.BLACK);

		URL urll = KhachHang.class.getResource("iconKH.png");
		Image img = Toolkit.getDefaultToolkit().createImage(urll);
		this.setIconImage(img);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("About me");
		mnNewMenu.setFont(new Font("Segoe UI", Font.BOLD, 20));
		mnNewMenu.setForeground(new Color(0, 0, 0));
		menuBar.add(mnNewMenu);

		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Users");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String mk = JOptionPane.showInputDialog("Nhập mật khẩu");
				if (mk.equals("123456")) {
					Users u = new Users();
					u.setVisible(true);
					setVisible(false);
				} else {
					JOptionPane.showMessageDialog(null, "Sai mật khẩu");
				}
			}
		});
		mntmNewMenuItem_2.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		mnNewMenu.add(mntmNewMenuItem_2);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton QL = new JButton("");
		QL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showOptionDialog(null, "Bạn muốn tiếp tục?", "Xác nhận",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

				if (result == JOptionPane.YES_OPTION) {
					SignIn s = new SignIn();
					s.setVisible(true);
					setVisible(false);
				}
			}
		});
		QL.setBounds(0, 10, 46, 32);
		contentPane.add(QL);
		QL.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(KhachHang.class.getResource("iconback.png"))));

		JLabel lblNewLabel = new JLabel("Mã Khách Hàng:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel.setBounds(167, 20, 126, 32);
		contentPane.add(lblNewLabel);

		Search_MaKH = new JTextField();
		Search_MaKH.setFont(new Font("Tahoma", Font.PLAIN, 15));
		Search_MaKH.setBounds(303, 21, 205, 32);
		contentPane.add(Search_MaKH);
		Search_MaKH.setColumns(10);

		JButton btnNewButton = new JButton("Search");
		btnNewButton.setIcon(
				new ImageIcon(Toolkit.getDefaultToolkit().createImage(KhachHang.class.getResource("iconSearch.png"))));
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField_makh.setEditable(true);

				String maKHSearch = Search_MaKH.getText();
				if (maKHSearch.isEmpty()) {
					String tx = JOptionPane
							.showInputDialog("Vui lòng nhập mã khách hàng(maKH) vào đây để truy xuất thông tin");
					Search_MaKH.setText(tx);
				} else {
					String sql = "Select * From KhachHang where maKH = " + maKHSearch + "";
					try {
						PreparedStatement pst = conn.prepareStatement(sql);
						ResultSet rst = pst.executeQuery();
						if (rst.next()) {
							textField_makh.setText(rst.getString("maKH"));
							textField_tenkh.setText(rst.getString("tenKH"));
							textField_gioiTinh.setText(rst.getString("gioiTinh"));
							textField_tuoi.setText(rst.getString("tuoi"));
							textField_diachi.setText(rst.getString("diaChi"));

							pst.close();
						} else {
							JOptionPane.showMessageDialog(null,
									"Không có thông tin về Mã Khách Hàng: '" + Search_MaKH.getText() + "'");
						}

					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnNewButton.setBounds(533, 19, 135, 34);
		contentPane.add(btnNewButton);

		JButton bt_update = new JButton("Update");
		bt_update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField_makh.setEditable(true);

				update();
				clearFields();

			}
		});

		bt_update.setIcon(
				new ImageIcon(Toolkit.getDefaultToolkit().createImage(KhachHang.class.getResource("iconUpdate.png"))));
		bt_update.setBounds(189, 450, 170, 51);
		contentPane.add(bt_update);
		bt_update.setFont(new Font("Tahoma", Font.BOLD, 20));

		JButton bt_delete = new JButton("Delete");
		bt_delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField_makh.setEditable(true);

				delete();
				clearFields();
			}
		});

		bt_delete.setIcon(
				new ImageIcon(Toolkit.getDefaultToolkit().createImage(KhachHang.class.getResource("iconDelete.png"))));

		bt_delete.setBounds(10, 450, 157, 51);
		contentPane.add(bt_delete);
		bt_delete.setFont(new Font("Tahoma", Font.BOLD, 20));

		JButton bt_insert = new JButton("Insert");
		bt_insert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField_makh.setEditable(true);
				insert();
				reload();

			}

		});

		bt_insert.setIcon(
				new ImageIcon(Toolkit.getDefaultToolkit().createImage(KhachHang.class.getResource("iconInsert.png"))));
		bt_insert.setBounds(10, 367, 157, 51);
		contentPane.add(bt_insert);
		bt_insert.setFont(new Font("Tahoma", Font.BOLD, 20));

		JLabel lblMakh = new JLabel("maKH");
		lblMakh.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblMakh.setBounds(10, 79, 53, 32);
		contentPane.add(lblMakh);

		JLabel lblTenkh = new JLabel("tenKH");
		lblTenkh.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblTenkh.setBounds(10, 145, 53, 32);
		contentPane.add(lblTenkh);

		JLabel lblTuoi = new JLabel("tuoi");
		lblTuoi.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblTuoi.setBounds(10, 243, 53, 32);
		contentPane.add(lblTuoi);

		JLabel lblDiachi = new JLabel("diaChi");
		lblDiachi.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblDiachi.setBounds(10, 309, 53, 32);
		contentPane.add(lblDiachi);

		textField_makh = new JTextField();
		textField_makh.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textField_makh.setColumns(10);
		textField_makh.setBounds(91, 82, 268, 32);
		contentPane.add(textField_makh);

		textField_tenkh = new JTextField();
		textField_tenkh.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textField_tenkh.setColumns(10);
		textField_tenkh.setBounds(91, 148, 268, 32);
		contentPane.add(textField_tenkh);

		textField_tuoi = new JTextField();
		textField_tuoi.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textField_tuoi.setColumns(10);
		textField_tuoi.setBounds(91, 246, 268, 32);
		contentPane.add(textField_tuoi);

		textField_diachi = new JTextField();
		textField_diachi.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textField_diachi.setColumns(10);
		textField_diachi.setBounds(91, 312, 268, 32);
		contentPane.add(textField_diachi);

		Box verticalBox = Box.createVerticalBox();
		verticalBox.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		verticalBox.setBounds(369, 464, 524, 32);
		contentPane.add(verticalBox);

		lblNote = new JLabel("NOTE");
		lblNote.setHorizontalAlignment(SwingConstants.LEFT);
		lblNote.setIcon(
				new ImageIcon(Toolkit.getDefaultToolkit().createImage(KhachHang.class.getResource("iconNote.png"))));
		lblNote.setFont(new Font("Tekton Pro", Font.BOLD, 15));
		lblNote.setBounds(379, 441, 64, 24);
		contentPane.add(lblNote);

		JButton bt_clear = new JButton("Clear");
		bt_clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField_diachi.setText("");
				textField_makh.setEditable(true);
				textField_makh.setText("");
				textField_gioiTinh.setText("");
				textField_tenkh.setText("");
				textField_tuoi.setText("");
			}
		});
		bt_clear.setIcon(
				new ImageIcon(Toolkit.getDefaultToolkit().createImage(KhachHang.class.getResource("iconClear.png"))));

		bt_clear.setFont(new Font("Tahoma", Font.BOLD, 20));
		bt_clear.setBounds(189, 367, 157, 51);
		contentPane.add(bt_clear);

		note = new JLabel("Mã khách hàng phải có 4 chữ số !");
		note.setBounds(379, 464, 514, 37);
		contentPane.add(note);
		note.setForeground(Color.RED);
		note.setFont(new Font("Tahoma", Font.ITALIC, 15));

		JLabel lblGioiTinh = new JLabel("Giới tính");
		lblGioiTinh.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblGioiTinh.setBounds(10, 190, 71, 32);
		contentPane.add(lblGioiTinh);

		nam = new JRadioButton("Nam");
		nam.setFont(new Font("Tahoma", Font.BOLD, 16));
		nam.setBounds(91, 196, 64, 21);
		contentPane.add(nam);
		nam.addActionListener(this);

		nu = new JRadioButton("Nữ");
		nu.setFont(new Font("Tahoma", Font.BOLD, 16));
		nu.setBounds(162, 196, 53, 21);
		contentPane.add(nu);
		nu.addActionListener(this);

		ButtonGroup btg = new ButtonGroup();
		btg.add(nam);
		btg.add(nu);

		reload();
		Model = new DefaultTableModel(vData, vTitle);// tạo 1 DefaultTableModel để hiển thị dữ liệu từ 2 vector đã tạo
		table = new JTable(Model);// thêm khung bảng 
		table.addMouseListener(this);// gắn ống nghe chuột
		tableResult = new JScrollPane(table);

		tableResult.setBounds(369, 63, 540, 377);
		contentPane.add(tableResult);

		tableResult.setViewportView(table);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 63, 361, 2);
		contentPane.add(separator_1);

		JSeparator separator_1_1 = new JSeparator();
		separator_1_1.setBounds(10, 355, 361, 2);
		contentPane.add(separator_1_1);

		JLabel background = new JLabel("");
		background.setBounds(0, 0, 909, 511);
		contentPane.add(background);
		background.setIcon(
				new ImageIcon(Toolkit.getDefaultToolkit().createImage(KhachHang.class.getResource("background.jpg"))));

		textField_gioiTinh = new JTextField();
		textField_gioiTinh.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textField_gioiTinh.setColumns(10);
		textField_gioiTinh.setBounds(240, 190, 119, 32);
		contentPane.add(textField_gioiTinh);

		setLocationRelativeTo(null); // hiển thị cửa sổ jframe ra ngay giữa màn hình
	}

	public void reload() {
		try {
			vTitle.clear();
			vData.clear();

			ResultSet rst = stm.executeQuery("Select * From KhachHang");

			ResultSetMetaData rstmeta = rst.getMetaData();// lấy kết quả từ câu lệnh truy vấn
			int num_column = rstmeta.getColumnCount();// thực hiện soi kết quả lấy cột

			for (int i = 1; i <= num_column; i++) {

				vTitle.add(rstmeta.getColumnLabel(i));
			}

			while (rst.next()) {
				Vector<String> row = new Vector<String>(num_column);
				for (int i = 1; i <= num_column; i++) {
					row.add(rst.getString(i));
				}
				vData.add(row);
			}
			rst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete() {
		if (textField_diachi.getText().equals("") || textField_makh.getText().equals("")
				|| textField_tenkh.getText().equals("") || textField_tuoi.getText().equals("")
				|| textField_gioiTinh.getText().equals("")) {

			// Tạo nội dung lỗi

			note.setText("Vui lòng chọn thông tin khách hàng muốn xóa !");
			note.setForeground(Color.RED);
			JOptionPane.showMessageDialog(null, "Vui lòng chọn thông tin khách hàng muốn xóa !");
			// Hiển thị lỗi
			note.setVisible(true);
		} else {
			try {

				String sql = "DELETE FROM KhachHang WHERE maKH = '" + textField_makh.getText() + "'";
				stm.executeUpdate(sql);

				vData.remove(selectedrow);
				this.reload();
				Model.fireTableDataChanged();
				JOptionPane.showMessageDialog(null, "Xóa thành công! ");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void insert() {
		if (textField_diachi.getText().equals("") || textField_makh.getText().equals("")
				|| textField_tenkh.getText().equals("") || textField_tuoi.getText().equals("")
				|| textField_gioiTinh.getText().equals("")) {

			// Tạo nội dung lỗi

			note.setText("Vui lòng điền đầy đủ thông tin !");
			note.setForeground(Color.RED);
			JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin !");
			// Hiển thị lỗi
			note.setVisible(true);

		} else if (!textField_makh.getText().matches("^[0-9]{4}$")) {
			JOptionPane.showMessageDialog(null, "mã khách hàng phải có 4 chữ số ");
			note.setText("mã khách hàng phải có 4 chữ số ");
		} else if (!textField_tenkh.getText().matches("^[a-zA-Z \\p{L}]+$")) {// \\p{L} phù hợp với bất kỳ ký tự chữ cái
																				// nào trong bất kỳ ngôn ngữ nào.
			JOptionPane.showMessageDialog(null, "Tên khách hàng không đúng!");
			note.setText("Tên khách hàng không đúng!");
		} else if (!textField_tuoi.getText().matches("^(?:[1-9]|[1-9][0-9]|100)$")) {// xác định các trường hợp xãy ra.
																						// (?:...) là một nhóm không ghi
																						// nhớ, được sử dụng để tạo các
																						// tùy chọn.
			JOptionPane.showMessageDialog(null, "Tuổi vượt quá giới hạn cho phép! ");
			note.setText("Tuổi vượt quá giới hạn cho phép! ");
		} else {
			try {
				int makh = Integer.parseInt(textField_makh.getText());
				String tenkh = textField_tenkh.getText();

				String gt = textField_gioiTinh.getText();
				int tuoi = Integer.parseInt(textField_tuoi.getText());
				String dc = textField_diachi.getText();
				String sql = "insert into KhachHang(maKH,tenKH,gioiTinh,tuoi,DiaChi) Values(" + makh + ",N'" + tenkh
						+ "',N'" + gt + "'," + tuoi + ", N'" + dc + "'" + ")";

				// Cập nhật vào csdl
				this.stm.executeUpdate(sql);
				// Cập nhật giao diện của sổ chính
				this.reload();
				this.Model.fireTableDataChanged();
				clearFields();
				JOptionPane.showMessageDialog(null, "Nhập thành công!");
				note.setText("...");
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Mã khách hàng đã tồn tại!");
				note.setText("Mã khách hàng đã tồn tại!");
			}
		}
	}

	public void update() {
		if (textField_diachi.getText().equals("") || textField_makh.getText().equals("")
				|| textField_tenkh.getText().equals("") || textField_tuoi.getText().equals("")
				|| textField_gioiTinh.getText().equals("")) {

			// Tạo nội dung lỗi

			note.setText("Vui lòng chọn thông tin khách hàng! ");
			note.setForeground(Color.RED);
			JOptionPane.showMessageDialog(null, "Chưa chọn thông tin khách hàng cần thay đổi !");
			// Hiển thị lỗi
			note.setVisible(true);
		} else if (!textField_makh.getText().matches("^[0-9]{4}$")) {
			JOptionPane.showMessageDialog(null, "mã khách hàng phải có 4 chữ số ");
			note.setText("mã khách hàng phải có 4 chữ số ");
		} else if (!textField_tenkh.getText().matches("^[a-zA-Z]+$")) {// dấu + thể hiện chứa ist nhất 1 hoặc nhiều hơn
			JOptionPane.showMessageDialog(null, "Tên khách hàng không đúng!");
			note.setText("Tên khách hàng không đúng!");
		} else if (!textField_tuoi.getText().matches("^(?:[1-9]|[1-9][0-9]|100)$")) {// xác định các trường hợp xãy ra
			JOptionPane.showMessageDialog(null, "Tuổi vượt quá giới hạn cho phép! ");
			note.setText("Tuổi vượt quá giới hạn cho phép! ");
		} else {

			try {

				int makh = Integer.parseInt(textField_makh.getText());
				String tenkh = textField_tenkh.getText();
				int tuoi = Integer.parseInt(textField_tuoi.getText());
				String dc = textField_diachi.getText();
				String gt = textField_gioiTinh.getText();

				String sql = "update KhachHang set tenKH = N'" + tenkh + "', gioiTinh = N'" + gt + "', tuoi = " + tuoi
						+ ", diaChi = N'" + dc + "' Where MaKH = " + makh;

				// Cập nhật vào csdl
				this.stm.executeUpdate(sql);
				// Cập nhật giao diện của sổ chính
				this.reload();
				this.Model.fireTableDataChanged();
				JOptionPane.showMessageDialog(null, "Cập nhật thành công! ");
				note.setText("");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void clearFields() {
		textField_makh.setText("");
		textField_tenkh.setText("");
		textField_gioiTinh.setText("");
		textField_tuoi.setText("");
		textField_diachi.setText("");
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KhachHang frame = new KhachHang();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int selectedrow = table.getSelectedRow();
		textField_makh.setText(Model.getValueAt(selectedrow, 0).toString());
		textField_makh.setEditable(false);
		textField_tenkh.setText(Model.getValueAt(selectedrow, 1).toString());
		textField_gioiTinh.setText(Model.getValueAt(selectedrow, 2).toString());
		textField_tuoi.setText(Model.getValueAt(selectedrow, 3).toString());
		textField_diachi.setText(Model.getValueAt(selectedrow, 4).toString());

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (nam.isSelected()) {
			textField_gioiTinh.setText("Nam");
		} else if (nu.isSelected()) {
			textField_gioiTinh.setText("Nữ");
		}

	}
}
