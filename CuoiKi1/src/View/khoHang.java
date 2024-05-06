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

public class khoHang extends JFrame implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;
	public JPanel contentPane;

	public JTextField Search_MaKH;
	public JTable table;
	public JTextField textField_makh;
	public JTextField textField_diachi;
	public JTextField textField_tonkho;
	public Component frame;

	public JLabel lblNote;
	public JLabel note;
	public JMenu dataTable;

	Vector<Vector<String>> vData = new Vector<Vector<String>>();
	Vector<String> vTitle = new Vector<String>();
	JScrollPane tableResult;
	DefaultTableModel Model;
	int selectedrow = 0;

	Statement stm;
	ResultSet rst;
	ConnectJDBC jdbc = new ConnectJDBC();
	Connection conn = null;

	public khoHang() {

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

		URL urll = khoHang.class.getResource("iconKH.png");
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

		JButton QL = new JButton("");
		QL.setBounds(0, 10, 46, 32);
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
		contentPane.setLayout(null);
		contentPane.add(QL);
		QL.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(khoHang.class.getResource("iconback.png"))));

		JLabel lblNewLabel = new JLabel("Tìm Kiếm Theo Mã KH:");
		lblNewLabel.setBounds(127, 20, 226, 32);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		contentPane.add(lblNewLabel);

		Search_MaKH = new JTextField();
		Search_MaKH.setBounds(303, 21, 205, 32);
		Search_MaKH.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPane.add(Search_MaKH);
		Search_MaKH.setColumns(10);

		JButton btnNewButton = new JButton("Tìm");
		btnNewButton.setBounds(533, 19, 106, 34);
		btnNewButton.setIcon(
				new ImageIcon(Toolkit.getDefaultToolkit().createImage(khoHang.class.getResource("iconSearch.png"))));
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
							textField_diachi.setText(rst.getString("tenKH"));
							
							textField_tonkho.setText(rst.getString("tuoi"));

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
		contentPane.add(btnNewButton);

		JButton bt_update = new JButton("Update");
		bt_update.setBounds(189, 450, 170, 51);
		bt_update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField_makh.setEditable(true);

				update();
				clearFields();

			}
		});

		bt_update.setIcon(
				new ImageIcon(Toolkit.getDefaultToolkit().createImage(khoHang.class.getResource("iconUpdate.png"))));
		contentPane.add(bt_update);
		bt_update.setFont(new Font("Tahoma", Font.BOLD, 20));

		JButton bt_delete = new JButton("Delete");
		bt_delete.setBounds(10, 450, 157, 51);
		bt_delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField_makh.setEditable(true);

				delete();
				clearFields();
			}
		});

		bt_delete.setIcon(
				new ImageIcon(Toolkit.getDefaultToolkit().createImage(khoHang.class.getResource("iconDelete.png"))));
		contentPane.add(bt_delete);
		bt_delete.setFont(new Font("Tahoma", Font.BOLD, 20));

		JButton bt_insert = new JButton("Insert");
		bt_insert.setBounds(10, 367, 157, 51);
		bt_insert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField_makh.setEditable(true);
				insert();
				reload();

			}

		});

		bt_insert.setIcon(
				new ImageIcon(Toolkit.getDefaultToolkit().createImage(khoHang.class.getResource("iconInsert.png"))));
		contentPane.add(bt_insert);
		bt_insert.setFont(new Font("Tahoma", Font.BOLD, 20));

		JLabel lblMakh = new JLabel("Mã Kho");
		lblMakh.setBounds(10, 79, 80, 32);
		lblMakh.setFont(new Font("Tahoma", Font.BOLD, 15));
		contentPane.add(lblMakh);

		JLabel diaChi = new JLabel("Địa Chỉ");
		diaChi.setBounds(10, 144, 80, 32);
		diaChi.setFont(new Font("Tahoma", Font.BOLD, 15));
		contentPane.add(diaChi);

		JLabel lblTuoi = new JLabel("Tồn Kho");
		lblTuoi.setBounds(10, 217, 63, 32);
		lblTuoi.setFont(new Font("Tahoma", Font.BOLD, 15));
		contentPane.add(lblTuoi);

		textField_makh = new JTextField();
		textField_makh.setBounds(91, 82, 268, 32);
		textField_makh.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textField_makh.setColumns(10);
		contentPane.add(textField_makh);

		textField_diachi = new JTextField();
		textField_diachi.setBounds(91, 148, 268, 32);
		textField_diachi.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textField_diachi.setColumns(10);
		contentPane.add(textField_diachi);

		textField_tonkho = new JTextField();
		textField_tonkho.setBounds(91, 220, 268, 32);
		textField_tonkho.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textField_tonkho.setColumns(10);
		contentPane.add(textField_tonkho);

		Box verticalBox = Box.createVerticalBox();
		verticalBox.setBounds(369, 464, 524, 32);
		verticalBox.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		contentPane.add(verticalBox);

		lblNote = new JLabel("NOTE");
		lblNote.setBounds(379, 441, 74, 24);
		lblNote.setHorizontalAlignment(SwingConstants.LEFT);
		lblNote.setIcon(
				new ImageIcon(Toolkit.getDefaultToolkit().createImage(khoHang.class.getResource("iconNote.png"))));
		lblNote.setFont(new Font("Tekton Pro", Font.BOLD, 15));
		contentPane.add(lblNote);

		JButton bt_clear = new JButton("Clear");
		bt_clear.setBounds(189, 367, 157, 51);
		bt_clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField_makh.setEditable(true);
				textField_makh.setText("");
				textField_diachi.setText("");
				textField_tonkho.setText("");
			}
		});
		bt_clear.setIcon(
				new ImageIcon(Toolkit.getDefaultToolkit().createImage(khoHang.class.getResource("iconClear.png"))));

		bt_clear.setFont(new Font("Tahoma", Font.BOLD, 20));
		contentPane.add(bt_clear);

		note = new JLabel("Mã khách hàng phải có 4 chữ số !");
		note.setBounds(379, 464, 514, 37);
		contentPane.add(note);
		note.setForeground(Color.RED);
		note.setFont(new Font("Tahoma", Font.ITALIC, 15));

		ButtonGroup btg = new ButtonGroup();

		reload();
		Model = new DefaultTableModel(vData, vTitle);
		table = new JTable(Model);
		table.addMouseListener(this);
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
				new ImageIcon(Toolkit.getDefaultToolkit().createImage(khoHang.class.getResource("background.jpg"))));

		setLocationRelativeTo(null); // hiển thị cửa sổ jframe ra ngay giữa màn hình
	}

	public void reload() {
		try {
			vTitle.clear();
			vData.clear();

			ResultSet rst = stm.executeQuery("Select * From KhachHang");

			ResultSetMetaData rstmeta = rst.getMetaData();
			int num_column = rstmeta.getColumnCount();

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
		if ( textField_makh.getText().equals("")
				|| textField_diachi.getText().equals("") || textField_tonkho.getText().equals("")){
				

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
		if ( textField_makh.getText().equals("")
				|| textField_diachi.getText().equals("") || textField_tonkho.getText().equals("")){
			// Tạo nội dung lỗi
			note.setText("Vui lòng điền đầy đủ thông tin !");
			note.setForeground(Color.RED);
			JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin !");
			// Hiển thị lỗi
			note.setVisible(true);

		} else if (!textField_makh.getText().matches("^[0-9]{4}$")) {
			JOptionPane.showMessageDialog(null, "mã khách hàng phải có 4 chữ số ");
			note.setText("mã khách hàng phải có 4 chữ số ");
		} else if (!textField_diachi.getText().matches("^[a-zA-Z \\p{L}]+$")) {// \\p{L} phù hợp với bất kỳ ký tự chữ cái
																				// nào trong bất kỳ ngôn ngữ nào.
			JOptionPane.showMessageDialog(null, "Tên khách hàng không đúng!");
			note.setText("Tên khách hàng không đúng!");
		} else if (!textField_tonkho.getText().matches("^(?:[1-9]|[1-9][0-9]|100)$")) {// xác định các trường hợp xãy ra.
																						// (?:...) là một nhóm không ghi
																						// nhớ, được sử dụng để tạo các
																						// tùy chọn.
			JOptionPane.showMessageDialog(null, "Tuổi vượt quá giới hạn cho phép! ");
			note.setText("Tuổi vượt quá giới hạn cho phép! ");
		} else {
			try {
				int makh = Integer.parseInt(textField_makh.getText());
				String tenkh = textField_diachi.getText();

				
				
				String sql = "insert into KhachHang(maKH,tenKH,gioiTinh,tuoi,DiaChi) Values(" + makh + ",N'" + tenkh
						+ ")";

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
				|| textField_diachi.getText().equals("") || textField_tonkho.getText().equals("")) {
				

			// Tạo nội dung lỗi

			note.setText("Vui lòng chọn thông tin khách hàng! ");
			note.setForeground(Color.RED);
			JOptionPane.showMessageDialog(null, "Chưa chọn thông tin khách hàng cần thay đổi !");
			// Hiển thị lỗi
			note.setVisible(true);
		} else if (!textField_makh.getText().matches("^[0-9]{4}$")) {
			JOptionPane.showMessageDialog(null, "mã khách hàng phải có 4 chữ số ");
			note.setText("mã khách hàng phải có 4 chữ số ");
		} else if (!textField_diachi.getText().matches("^[a-zA-Z]+$")) {// dấu + thể hiện chứa ist nhất 1 hoặc nhiều hơn
			JOptionPane.showMessageDialog(null, "Tên khách hàng không đúng!");
			note.setText("Tên khách hàng không đúng!");
		} else if (!textField_tonkho.getText().matches("^(?:[1-9]|[1-9][0-9]|100)$")) {// xác định các trường hợp xãy ra
			JOptionPane.showMessageDialog(null, "Tuổi vượt quá giới hạn cho phép! ");
			note.setText("Tuổi vượt quá giới hạn cho phép! ");
		} else {

			try {

				int makh = Integer.parseInt(textField_makh.getText());
				String tenkh = textField_diachi.getText();
				int tuoi = Integer.parseInt(textField_tonkho.getText());
				String dc = textField_diachi.getText();
				

				String sql = "update KhachHang set tenKH = N'" + tenkh + "', tuoi = " + tuoi
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
		textField_diachi.setText("");
		textField_tonkho.setText("");
		textField_diachi.setText("");
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					khoHang frame = new khoHang();
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
		textField_diachi.setText(Model.getValueAt(selectedrow, 1).toString());
		textField_tonkho.setText(Model.getValueAt(selectedrow, 3).toString());
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
		}

	}

