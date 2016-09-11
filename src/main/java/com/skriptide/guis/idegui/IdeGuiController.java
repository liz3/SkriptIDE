package com.skriptide.guis.idegui;

import com.skriptide.codemanage.CodeReader;
import com.skriptide.codemanage.CodeWriter;
import com.skriptide.codemanage.ControlMain;
import com.skriptide.codemanage.Supers;
import com.skriptide.guis.SceneManager;
import com.skriptide.main.Main;
import com.skriptide.util.Config;
import com.skriptide.util.DragResizer;
import com.skriptide.util.MCServer;
import com.skriptide.util.Project;
import com.skriptide.util.skunityapi.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.PopupAlignment;
import org.fxmisc.richtext.StyleClassedTextArea;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Liz3ga on 27.07.2016.
 */
public class IdeGuiController {

	private static SkUnityAPI skUnity = new SkUnityAPI();
	@FXML
	public TabPane codeTabPane;
	@FXML
	public Label searchLabel;
	@FXML
	public CodeArea consoleOutputTextArea;
	@FXML
	public Label runningServerLabel;
	@FXML
	public ListView<String> projectsList;
	@FXML
	public TextField comandSendTextField;
	@FXML
	public Button commandSendBtn;
	@FXML
	public Button stopServerBtn;
	@FXML
	public Button restartServerBtn;
	@FXML
	public ComboBox<String> serverListComboBox;
	@FXML
	public MenuItem runPoint;
	@FXML
	public Label pathLabel;
	@FXML
	public MenuItem debuggingPoint;
	@FXML
	public ProgressBar mainProcessBar;
	@FXML
	public BorderPane mainBorderPane;
	@FXML
	public VBox lowerBox;
	@FXML
	public TabPane lowerTabPane;

	private SceneManager sceneManager = new SceneManager();
	private ListView<String> chooseView;
	private boolean showList;
	private Popup win;
	private ArrayList<String> all = new ArrayList<>();
	private int pos = 0;
	private ContextMenu menu;
	private double y;

	private void setList() {


		if (win == null) {
			win = new Popup();
			chooseView = new ListView();
		}

		ArrayList<ApiCondition> conditions = skUnity.getConditions();
		ArrayList<ApiEffect> effects = skUnity.getEffects();
		ArrayList<ApiEvent> events = skUnity.getEvents();
		ArrayList<ApiExpression> expressions = skUnity.getExpressions();
		ArrayList<ApiType> types = skUnity.getTypes();
		for (int i = 1; i != conditions.size(); i++) {
			chooseView.getItems().add(conditions.get(i).getId() + " Condition");
		}
		for (int i = 0; i != effects.size(); i++) {
			chooseView.getItems().add(effects.get(i).getId() + " Effect");
		}
		for (int i = 0; i != events.size(); i++) {
			chooseView.getItems().add(events.get(i).getId() + " Event");
		}
		for (int i = 0; i != expressions.size(); i++) {
			chooseView.getItems().add(expressions.get(i).getId() + " Expression");
		}
		for (int i = 0; i != types.size(); i++) {
			chooseView.getItems().add(types.get(i).getId() + " Type");
		}

		chooseView.getItems().addAll(new Supers().getSupervArray());

		chooseView.setPrefSize(180, 200);
		Tab tab = codeTabPane.getSelectionModel().getSelectedItem();
		CodeArea area = (CodeArea) tab.getContent();
		win.getContent().add(chooseView);

		area.setPopupWindow(win);


		area.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (showList) {

					win.hide();
					chooseView.setVisible(false);
					showList = false;

				}
			}
		});

		if (Main.debugMode) {
			System.out.println("loaded list");
		}
	}

	public void chooseList() {
		Tab tab = codeTabPane.getSelectionModel().getSelectedItem();
		CodeArea area = (CodeArea) tab.getContent();

		if (win == null) {
			setList();

		}


		if (!win.isShowing()) {

			Stage stage = (Stage) commandSendBtn.getScene().getWindow();


			area.setPopupAlignment(PopupAlignment.CARET_BOTTOM);
			win.show(stage);
			chooseView.setVisible(true);
			showList = true;
			if (Main.debugMode) {
				System.out.println("showed list");
			}

		}
		updateList();


	}


	public void updateList() {

		Tab tab = codeTabPane.getSelectionModel().getSelectedItem();
		CodeArea area = (CodeArea) tab.getContent();


		area.caretPositionProperty().addListener((obs, oldPosition, newPosition) -> {

			String text = area.getText().substring(0, newPosition.intValue());
			int index;
			for (index = text.length() - 1; index >= 0 && !Character.isWhitespace(text.charAt(index)); index--) ;
			String prefix = text.substring(index + 1, text.length());

			System.out.println(prefix);
			if (chooseView.isVisible()) {
				javafx.application.Platform.runLater(() -> {


							chooseView.getItems().sorted().stream().filter(str -> !str.toLowerCase().contains(prefix.toLowerCase())).forEach(str -> {
								chooseView.getItems().remove(str);
								all.add(str);
								chooseView.refresh();
							});

							ArrayList<String> toRemove = new ArrayList<String>();
							for (int i = 0; i != all.size(); i++) {
								String current = all.get(i);
								if (current.toLowerCase().contains(prefix.toLowerCase())) {
									chooseView.getItems().add(current);
									toRemove.add(current);
									chooseView.refresh();
								}
							}
							for (int i = 0; i != toRemove.size(); i++) {
								String rem = toRemove.get(i);
								if (all.contains(rem)) {
									all.remove(rem);
								}
							}

						}


				);
			}
			chooseView.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					KeyCode code = event.getCode();

					if (code == KeyCode.ESCAPE) {

						System.out.println("casted");
						if (showList) {

							win.hide();
							chooseView.setVisible(false);
							showList = false;

						}
					} else if (code == KeyCode.ENTER) {
						if (showList) {

							setWord(prefix);

						}
					}
				}
			});
			chooseView.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {


					setWord(prefix);

				}

			});

		});

		if (Main.debugMode) {
			System.out.println("Updatet list");
		}
	}

	private void setWord(String prefix) {


		Tab tab = codeTabPane.getSelectionModel().getSelectedItem();
		CodeArea area = (CodeArea) tab.getContent();
		String seletion = chooseView.getSelectionModel().getSelectedItem();
		String before = "";
		if (seletion.contains("Event")) {

			String trueT = "";
			String[] split = seletion.split(" ");
			String[] r = split[0].split("(?=\\p{Upper})");
			for (int i = 0; i != r.length; i++) {
				trueT = trueT + " " + r[i];
			}


			before = area.getText(area.getCaretPosition(), area.getCaretPosition() - prefix.length() + trueT.trim().length() * 2);


			area.replaceText(area.getCaretPosition() - prefix.length(), area.getCaretPosition() - prefix.length() + trueT.trim().length() * 2, trueT.trim() + ":\n        " + before);


			pos = area.getCaretPosition() - before.length();

		} else {
			String trueT = "";
			String[] split = seletion.split(" ");
			trueT = split[0];


			before = area.getText(area.getCaretPosition(), area.getCaretPosition() - prefix.length() + trueT.trim().length() * 2);

			area.replaceText(area.getCaretPosition() - prefix.length(), area.getCaretPosition() - prefix.length() + trueT.trim().length() * 2, trueT.trim() + " " + before);


			pos = area.getCaretPosition() - before.length();
		}
		System.out.println(pos);
		area.moveTo(pos);


		if (showList) {
			win.hide();
			chooseView.setVisible(false);
			showList = false;


		}
		if (Main.debugMode) {
			System.out.println("set word: " + prefix);
		}
	}

	public void setUpWin() {

		DragResizer.makeResizable(lowerTabPane);


		comandSendTextField.setOnKeyPressed(event -> {

			KeyCode code = event.getCode();

			if (code == KeyCode.ENTER) {
				sendCommand();
			}
		});
		SceneManager.procBar = this.mainProcessBar;
		SceneManager.consoleOut = consoleOutputTextArea;
		SceneManager.runningServerLabel = runningServerLabel;
		SceneManager.projectsList = this.projectsList;
		SceneManager.runninServerList = this.serverListComboBox;


		codeTabPane.getSelectionModel().selectedItemProperty().addListener(
				new ChangeListener<Tab>() {
					@Override
					public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
						Tab tab = codeTabPane.getSelectionModel().getSelectedItem();


						if (tab != null) {
							String name = tab.getText();
							ObservableList<Project> prs = Project.getProjects();
							for (Project project : prs.sorted()) {
								if (project.getName().equalsIgnoreCase(name)) {

									pathLabel.setText(project.getSkriptPath());
								}
							}
						}
					}
				}
		);

		if (Main.debugMode) {
			System.out.println("Loaded window");
		}
	}

	public void sendCommand() {
		if (SceneManager.runningServer != null) {
			try {
				BufferedWriter writer = SceneManager.runningServer.getWriter();


				writer.write(comandSendTextField.getText());
				writer.newLine();
				System.out.println("wrote:" + comandSendTextField.getText());
				writer.flush();
				comandSendTextField.clear();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (Main.debugMode) {
			System.out.println("sendet command");
		}
	}


	public void runProject() {

		Tab tab = codeTabPane.getSelectionModel().getSelectedItem();
		String name = tab.getText();

		ObservableList<Project> prs = Project.getProjects();
		for (Project project : prs.sorted()) {
			if (project.getName().equalsIgnoreCase(name)) {

				serverListComboBox.getSelectionModel().select(project.getServer().getname());
				project.runProject();
			}
		}
		if (Main.debugMode) {

			System.out.println("started project");
		}
	}

	public void loadInProjects() {


		if (Config.checkConfig() == 0) {
			ObservableList<Project> projects = Project.getProjects();
			System.out.println(projects.size());
			projectsList.getItems().clear();
			for (Project project : projects.sorted()) {
				if (project.getName() != null) {
					projectsList.getItems().add(project.getName());
				}
			}
		}
		if (Main.debugMode) {

			System.out.println("Loaded projects");
		}

		projectsList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {

				MouseButton btn = event.getButton();

				if (projectsList.getSelectionModel().getSelectedItem() != null) {
					if (btn == MouseButton.SECONDARY) {
						if (menu == null || !menu.isShowing()) {
							menu = new ContextMenu();
							MenuItem item = new MenuItem("Delete");
							MenuItem item1 = new MenuItem("Rename");
							MenuItem item2 = new MenuItem("Move");
							Menu serverList = new Menu("Change server");
							item.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									ObservableList<Project> prs = Project.getProjects();
									for (Project pr : prs) {
										if (pr.getName().equals(projectsList.getSelectionModel().getSelectedItem())) {
											pr.deleteProject();
										}
									}
								}
							});
							item1.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									ObservableList<Project> prs = Project.getProjects();
									for (Project pr : prs) {
										if (pr.getName().equals(projectsList.getSelectionModel().getSelectedItem())) {
											pr.reNameProject();
										}
									}
								}
							});
							item2.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									ObservableList<Project> prs = Project.getProjects();
									for (Project pr : prs) {
										if (pr.getName().equals(projectsList.getSelectionModel().getSelectedItem())) {
											pr.moveProject();
										}
									}
								}
							});
							for (MenuItem selected : serverList.getItems()) {

								selected.setOnAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {
										ObservableList<Project> prs = Project.getProjects();
										for (Project pr : prs) {
											if (pr.getName().equals(projectsList.getSelectionModel().getSelectedItem())) {
												pr.changeServer(selected.getText());
											}
										}
									}
								});
							}


							ObservableList<MCServer> servers = MCServer.getAllServers();
							ObservableList<Project> prs = Project.getProjects();
							for (MCServer srv : servers.sorted()) {
								for (Project pr : prs.sorted()) {
									if (pr.getName().equalsIgnoreCase(projectsList.getSelectionModel().getSelectedItem())) {
										if (pr.getServer().getname().equalsIgnoreCase(srv.getname())) {
											MenuItem srvItem = new MenuItem(srv.getname() + " (Current)");
											serverList.getItems().add(srvItem);
										} else {
											MenuItem srvItem = new MenuItem(srv.getname());
											serverList.getItems().add(srvItem);
										}
									}
								}
							}
							menu.getItems().clear();
							menu.getItems().addAll(item, item1, item2, serverList);


							menu.show(projectsList, event.getScreenX(), event.getScreenY());
						}
					} else if (btn == MouseButton.PRIMARY) {

						if (menu == null || !menu.isShowing()) {
							openProject();
						} else {
							if (menu.isShowing() && menu != null) {
								menu.hide();
							}
						}
					}

				}
			}
		});
	}

	public void triggerDebugger() {
		sceneManager.openDebugger();
	}

	public void loadInServers() {

		serverListComboBox.getItems().setAll();
		ObservableList<MCServer> servers = MCServer.getAllServers();
		for (MCServer srv : servers.sorted()) {
			serverListComboBox.getItems().add(srv.getname());
		}

		if (Main.debugMode) {
			System.out.println("Loaded servers");
		}

	}

	public void startServer() {

		ObservableList<MCServer> servers = MCServer.getAllServers();
		for (MCServer srv : servers.sorted()) {
			if (srv.getname().equalsIgnoreCase(serverListComboBox.getSelectionModel().getSelectedItem())) {
				srv.startServer();
			}
		}
		if (Main.debugMode) {

			System.out.println("start server");
		}
	}

	public void newProject() {

		sceneManager.openCreateProject();

		if (Main.debugMode) {
			System.out.println("open new project");
		}

	}

	public void newServer() {

		sceneManager.openCreateServer();
		if (Main.debugMode) {
			System.out.println("open new server");
		}
	}

	public void manageAddons() throws IOException {

		sceneManager.openManageVersions();
		if (Main.debugMode) {

			System.out.println("open manage addons");
		}

	}

	public void manageServers() {

		sceneManager.openManageServer();
		if (Main.debugMode) {
			System.out.println("open manage server");
		}
	}


	public void openProject() {
		CodeArea area = new CodeArea();


		area.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {

				KeyCode code = event.getCode();
				if (event.isControlDown() && code == KeyCode.SPACE) {
					chooseList();
					updateList();

				}


			}
		});

		String selection = projectsList.getSelectionModel().getSelectedItem();


		if (selection != null) {
			Project project = new Project(selection);
			if (!SceneManager.openProjects.contains(project.getName())) {
				Tab tab = new Tab(project.getName());

				tab.setOnCloseRequest(new EventHandler<Event>() {
					@Override
					public void handle(Event event) {


						boolean toSave = new SceneManager().infoCheck("Save project?", tab.getText(), "Save the project: " + tab.getText());
						if (toSave) {
							CodeWriter writer = new CodeWriter(area.getText(), project);
							System.out.println("saver called");
							writer.write();
						}

						SceneManager.openProjects.remove(project.getName());
					}
				});
				codeTabPane.getTabs().add(tab);

				tab.setContent(area);

				File skript = new File(project.getSkriptPath().replace("/", "/"));


				CodeReader reader = new CodeReader(skript);

				area.appendText(reader.getCode());


				SceneManager.openProjects.add(project.getName());

				ControlMain.controlCode(area);
				System.out.println("project open?");
			}

		}
		if (Main.debugMode) {
			System.out.println("Open project");
		}
	}


	public void saveOpenProjects() {

		for (Tab tab : codeTabPane.getTabs().sorted()) {
			StyleClassedTextArea area = (StyleClassedTextArea) tab.getContent();

			Project pr = new Project(tab.getText());


			CodeWriter writer = new CodeWriter(area.getText(), pr);
			System.out.println("saver called");
			writer.write();


		}
		if (Main.debugMode) {
			System.out.println("Saved open projects!");
		}
	}


}
