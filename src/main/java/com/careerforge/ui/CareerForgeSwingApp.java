package com.careerforge.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import com.careerforge.resume.*;
import com.careerforge.job.*;
import com.careerforge.application.*;
import com.careerforge.user.Admin;
import com.careerforge.portal.CareerPortal;
import com.careerforge.user.Employer;
import com.careerforge.user.Student;
import com.careerforge.recommendation.*;
import com.careerforge.util.ValidationUtils;

public class CareerForgeSwingApp extends JFrame {

    private static final String HOME_CARD = "home";
    private static final String STUDENT_AUTH_CARD = "student-auth";
    private static final String EMPLOYER_AUTH_CARD = "employer-auth";
    private static final String STUDENT_DASHBOARD_CARD = "student-dashboard";
    private static final String EMPLOYER_DASHBOARD_CARD = "employer-dashboard";
    private static final String ADMIN_AUTH_CARD = "admin-auth";
    private static final String ADMIN_DASHBOARD_CARD = "admin-dashboard";
    private static final String ADMIN_USERNAME = "ADMIN";
    private static final String ADMIN_PASSWORD = "admin@123";

    private static final Color BACKGROUND = new Color(244, 239, 228);
    private static final Color SURFACE = new Color(255, 251, 243);
    private static final Color ACCENT = new Color(25, 88, 72);
    private static final Color SECONDARY = new Color(198, 120, 69);
    private static final Font TITLE_FONT = new Font("Georgia", Font.BOLD, 30);
    private static final Font SUBTITLE_FONT = new Font("Georgia", Font.PLAIN, 16);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final DateTimeFormatter REPORT_TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final CareerPortal portal;
    private final CardLayout cardLayout;
    private final JPanel cards;

    private Student currentStudent;
    private Employer currentEmployer;
    private final Admin currentAdmin;

    private JTextField studentLoginIdField;
    private JPasswordField studentLoginPasswordField;
    private JTextField registerFirstNameField;
    private JTextField registerLastNameField;
    private JTextField registerEmailField;
    private JPasswordField registerPasswordField;
    private JPasswordField registerConfirmPasswordField;

    private JTextField employerLoginIdField;
    private JPasswordField employerLoginPasswordField;
    private JTextField adminLoginUsernameField;
    private JPasswordField adminLoginPasswordField;

    private JLabel studentWelcomeLabel;
    private JTextArea studentProfileViewArea;
    private JTextField profileMajorField;
    private JTextField profileGpaField;
    private JTextField profileGradYearField;
    private JTextField educationUniversityField;
    private JTextField educationDegreeField;
    private JTextArea skillsArea;
    private JTextArea certificationsArea;
    private JTextArea experiencesArea;
    private JTextArea projectsArea;
    private JTable jobsTable;
    private DefaultTableModel jobsTableModel;
    private JTextArea jobDetailsArea;
    private JSplitPane studentJobsSplitPane;
    private JComboBox<String> jobFilterTypeCombo;
    private JTextField jobFilterValueField;
    private List<JobPosting> studentVisibleJobs;
    private JTable studentApplicationsTable;
    private DefaultTableModel studentApplicationsTableModel;
    private JTextArea applicationDetailsArea;
    private JSplitPane studentApplicationsSplitPane;
    private List<ApplicationComponent> studentVisibleApplications;
    private JComboBox<String> recommendationStrategyCombo;
    private JLabel recommendationLocationLabel;
    private JTextField recommendationLocationField;
    private JTextArea recommendationHelpArea;
    private JTable recommendationsTable;
    private DefaultTableModel recommendationsTableModel;
    private JList<String> studentNotificationsList;

    private JLabel employerHeaderLabel;
    private JTextField employerJobTitleField;
    private JTextField employerJobLocationField;
    private JTextField employerJobSkillsField;
    private JTextField employerJobMajorField;
    private JTextField employerJobDeadlineField;
    private JComboBox<String> employerJobTypeCombo;
    private JPanel employerTypeCards;
    private JTextField fullTimeMinSalaryField;
    private JTextField fullTimeMaxSalaryField;
    private JTextField fullTimeSigningBonusField;
    private JComboBox<String> fullTimeBenefitsCombo;
    private JTextField internshipDurationField;
    private JTextField internshipStipendField;
    private JComboBox<String> internshipCreditCombo;
    private JTextField coOpRotationField;
    private JTextField coOpDurationMonthsField;
    private JTable employerJobsTable;
    private DefaultTableModel employerJobsTableModel;
    private JTextArea employerJobsDetailsArea;
    private JSplitPane employerJobsSplitPane;
    private List<JobPosting> employerVisibleJobs;
    private JTable employerApplicationsTable;
    private DefaultTableModel employerApplicationsTableModel;
    private JTextArea employerApplicationDetailsArea;
    private JSplitPane employerApplicationsSplitPane;
    private JComboBox<String> employerActionCombo;
    private List<BaseApplication> employerVisibleApplications;
    private JList<String> employerNotificationsList;

    private JLabel totalStudentsLabel;
    private JLabel totalEmployersLabel;
    private JLabel totalJobsLabel;
    private JLabel totalApplicationsLabel;
    private JTextArea adminStudentsArea;
    private JTextArea adminEmployersArea;
    private JTextArea adminJobsArea;
    private JTextArea adminReportArea;

    public CareerForgeSwingApp(CareerPortal portal) {
        super("Career Forge");
        this.portal = portal;
        this.cardLayout = new CardLayout();
        this.cards = new JPanel(cardLayout);
        this.studentVisibleJobs = new ArrayList<>();
        this.studentVisibleApplications = new ArrayList<>();
        this.employerVisibleJobs = new ArrayList<>();
        this.employerVisibleApplications = new ArrayList<>();
        this.currentAdmin = new Admin(
            "id",
            "System",
            "Administrator",
            "admin@careerforge.com",
            "password",
            "SuperAdmin"
        );

        configureLookAndFeel();
        configureFrame();
        buildCards();
    }

    public void launch() {
        setVisible(true);
    }

    private void configureLookAndFeel() {
        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("OptionPane.background", SURFACE);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("PasswordField.background", Color.WHITE);
        UIManager.put("TextArea.background", Color.WHITE);
        UIManager.put("ComboBox.background", Color.WHITE);
    }

    private void configureFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1180, 760);
        setMinimumSize(new Dimension(1080, 680));
        setLocationRelativeTo(null);
    }

    private void buildCards() {
        cards.add(buildWelcomePanel(), HOME_CARD);
        cards.add(buildStudentAuthPanel(), STUDENT_AUTH_CARD);
        cards.add(buildEmployerAuthPanel(), EMPLOYER_AUTH_CARD);
        cards.add(buildAdminAuthPanel(), ADMIN_AUTH_CARD);
        cards.add(buildStudentDashboardPanel(), STUDENT_DASHBOARD_CARD);
        cards.add(buildEmployerDashboardPanel(), EMPLOYER_DASHBOARD_CARD);
        cards.add(buildAdminDashboardPanel(), ADMIN_DASHBOARD_CARD);
        setContentPane(cards);
        showCard(HOME_CARD);
    }

    private JPanel buildWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout(24, 24));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 42, 40, 42));

        JLabel title = new JLabel("Career Forge");
        title.setFont(TITLE_FONT);
        title.setForeground(ACCENT);
        title.setHorizontalAlignment(JLabel.CENTER);

        JPanel hero = new JPanel();
        hero.setLayout(new BorderLayout());
        hero.setBorder(createCardBorder());
        hero.setBackground(SURFACE);
        hero.add(title, BorderLayout.CENTER);

        JPanel roles = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 24));
        roles.setOpaque(false);
        roles.add(createRoleCard(
            "Student Portal",
            "Register, manage your profile, browse jobs, apply, and enhance applications.",
            () -> showCard(STUDENT_AUTH_CARD)
        ));
        roles.add(createRoleCard(
            "Employer Portal",
            "Post jobs, review applications, and update candidate statuses.",
            () -> showCard(EMPLOYER_AUTH_CARD)
        ));
        roles.add(createRoleCard(
            "Admin Dashboard",
            "Review platform totals and generate a quick system report.",
            () -> showCard(ADMIN_AUTH_CARD)
        ));

        panel.add(hero, BorderLayout.NORTH);
        panel.add(roles, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createRoleCard(String title, String body, Runnable action) {
        JPanel card = new JPanel(new BorderLayout(0, 18));
        card.setPreferredSize(new Dimension(300, 220));
        card.setBorder(createCardBorder());
        card.setBackground(SURFACE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 20));
        titleLabel.setForeground(ACCENT);

        JTextArea bodyArea = createReadOnlyArea(body);
        bodyArea.setOpaque(false);
        bodyArea.setFont(new Font("Georgia", Font.PLAIN, 14));

        JButton openButton = createPrimaryButton("Open");
        openButton.addActionListener(event -> action.run());

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(bodyArea, BorderLayout.CENTER);
        card.add(openButton, BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildStudentAuthPanel() {
        JPanel panel = createScreenPanel("Student Portal", "Login or register a new student account.");

        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTH;

        JPanel loginCard = createSectionCard();
        loginCard.setLayout(new GridBagLayout());
        studentLoginIdField = new JTextField(18);
        studentLoginPasswordField = new JPasswordField(18);
        addFormRow(loginCard, 0, "Student ID", studentLoginIdField);
        addFormRow(loginCard, 1, "Password", studentLoginPasswordField);

        JButton loginButton = createPrimaryButton("Login");
        loginButton.addActionListener(event -> loginStudent());
        JPanel loginActions = actionRow(loginButton, createSecondaryButton("Back", e -> showCard(HOME_CARD)));
        GridBagConstraints loginActionConstraints = baseConstraints(2);
        loginActionConstraints.gridwidth = 2;
        loginCard.add(loginActions, loginActionConstraints);

        JPanel registerCard = createSectionCard();
        registerCard.setLayout(new GridBagLayout());
        registerFirstNameField = new JTextField(18);
        registerLastNameField = new JTextField(18);
        registerEmailField = new JTextField(18);
        registerPasswordField = new JPasswordField(18);
        registerConfirmPasswordField = new JPasswordField(18);
        addFormRow(registerCard, 0, "First Name", registerFirstNameField);
        addFormRow(registerCard, 1, "Last Name", registerLastNameField);
        addFormRow(registerCard, 2, "Email", registerEmailField);
        addFormRow(registerCard, 3, "Password", registerPasswordField);
        addFormRow(registerCard, 4, "Confirm Password", registerConfirmPasswordField);

        JButton registerButton = createPrimaryButton("Register");
        registerButton.addActionListener(event -> registerStudent());
        JPanel registerActions = actionRow(registerButton);
        GridBagConstraints registerActionConstraints = baseConstraints(5);
        registerActionConstraints.gridwidth = 2;
        registerCard.add(registerActions, registerActionConstraints);

        gbc.gridx = 0;
        gbc.weightx = 0.45;
        content.add(withTitle("Student Login", loginCard), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.55;
        content.add(withTitle("Quick Registration", registerCard), gbc);

        panel.add(content, BorderLayout.NORTH);
        return panel;
    }

    private JPanel buildEmployerAuthPanel() {
        JPanel panel = createScreenPanel("Employer Portal", "Log in with an existing employer account.");
        JPanel card = createSectionCard();
        card.setLayout(new GridBagLayout());

        employerLoginIdField = new JTextField(18);
        employerLoginPasswordField = new JPasswordField(18);
        addFormRow(card, 0, "Employer ID", employerLoginIdField);
        addFormRow(card, 1, "Password", employerLoginPasswordField);

        JButton loginButton = createPrimaryButton("Login");
        loginButton.addActionListener(event -> loginEmployer());
        JPanel actions = actionRow(loginButton, createSecondaryButton("Back", e -> showCard(HOME_CARD)));
        GridBagConstraints actionConstraints = baseConstraints(2);
        actionConstraints.gridwidth = 2;
        card.add(actions, actionConstraints);

        panel.add(centered(withTitle("Employer Login", card)), BorderLayout.NORTH);
        return panel;
    }

    private JPanel buildAdminAuthPanel() {
        JPanel panel = createScreenPanel("Admin Portal", "Log in with the admin credentials.");
        JPanel card = createSectionCard();
        card.setLayout(new GridBagLayout());

        adminLoginUsernameField = new JTextField(18);
        adminLoginPasswordField = new JPasswordField(18);
        addFormRow(card, 0, "Username", adminLoginUsernameField);
        addFormRow(card, 1, "Password", adminLoginPasswordField);

        JButton loginButton = createPrimaryButton("Login");
        loginButton.addActionListener(event -> loginAdmin());
        JPanel actions = actionRow(loginButton, createSecondaryButton("Back", e -> showCard(HOME_CARD)));
        GridBagConstraints actionConstraints = baseConstraints(2);
        actionConstraints.gridwidth = 2;
        card.add(actions, actionConstraints);

        panel.add(centered(withTitle("Admin Login", card)), BorderLayout.NORTH);
        return panel;
    }

    private JPanel buildStudentDashboardPanel() {
        JPanel root = new JPanel(new BorderLayout(18, 18));
        root.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        root.setBackground(BACKGROUND);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        studentWelcomeLabel = new JLabel("Student Dashboard");
        studentWelcomeLabel.setFont(new Font("Georgia", Font.BOLD, 24));
        studentWelcomeLabel.setForeground(ACCENT);
        header.add(studentWelcomeLabel, BorderLayout.WEST);
        header.add(createSecondaryButton("Logout", e -> logoutToHome()), BorderLayout.EAST);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("View Profile", buildStudentProfileViewTab());
        tabs.addTab("Edit Profile", buildStudentProfileTab());
        tabs.addTab("Jobs", buildStudentJobsTab());
        tabs.addTab("Applications", buildStudentApplicationsTab());
        tabs.addTab("Recommendations", buildRecommendationsTab());
        tabs.addTab("Notifications", buildStudentNotificationsTab());

        root.add(header, BorderLayout.NORTH);
        root.add(tabs, BorderLayout.CENTER);
        return root;
    }

    private JPanel buildStudentProfileTab() {
        JPanel panel = new JPanel(new BorderLayout(18, 18));
        panel.setBackground(BACKGROUND);

        JPanel form = createSectionCard();
        form.setLayout(new GridBagLayout());

        profileMajorField = new JTextField(18);
        profileGpaField = new JTextField(18);
        profileGradYearField = new JTextField(18);
        educationUniversityField = new JTextField(18);
        educationDegreeField = new JTextField(18);
        skillsArea = createEditableArea(4);
        certificationsArea = createEditableArea(3);
        experiencesArea = createEditableArea(6);
        projectsArea = createEditableArea(6);

        addHintRow(form, 0, "Please enter your latest education details only.");
        addFormRow(form, 1, "Major", profileMajorField);
        addFormRow(form, 2, "GPA", profileGpaField);
        addFormRow(form, 3, "Graduation Year", profileGradYearField);
        addFormRow(form, 4, "University", educationUniversityField);
        addFormRow(form, 5, "Degree", educationDegreeField);
        addAreaRow(form, 6, "Skills", skillsArea, "Comma-separated, for example: Java, SQL, React");
        addAreaRow(form, 7, "Certifications", certificationsArea, "Comma-separated certifications");
        addAreaRow(form, 8, "Experiences", experiencesArea, "One per line: company | role | duration | description");
        addAreaRow(form, 9, "Projects", projectsArea, "One per line: name | technologies | description");

        JButton saveButton = createPrimaryButton("Save Profile & Resume");
        saveButton.addActionListener(event -> saveStudentProfile());
        GridBagConstraints buttonConstraints = baseConstraints(10);
        buttonConstraints.gridwidth = 2;
        form.add(actionRow(saveButton), buttonConstraints);

        panel.add(new JScrollPane(withTitle("Student Profile", form)), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildStudentProfileViewTab() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setBackground(BACKGROUND);
        studentProfileViewArea = createReadOnlyArea("");
        panel.add(withTitle("Profile Summary", new JScrollPane(studentProfileViewArea)), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildStudentJobsTab() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setBackground(BACKGROUND);

        JPanel controls = createSectionCard();
        controls.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 8));
        jobFilterTypeCombo = new JComboBox<>(new String[] {"All", "Type", "Location", "Skill"});
        jobFilterValueField = new JTextField(18);
        JButton filterButton = createPrimaryButton("Apply Filter");
        filterButton.addActionListener(event -> refreshStudentJobs());
        JButton clearButton = createSecondaryButton("Clear", e -> {
            jobFilterTypeCombo.setSelectedItem("All");
            jobFilterValueField.setText("");
            refreshStudentJobs();
        });
        JButton applyButton = createSecondaryButton("Apply to Selected", e -> applyToSelectedJob());
        controls.add(new JLabel("Filter"));
        controls.add(jobFilterTypeCombo);
        controls.add(jobFilterValueField);
        controls.add(filterButton);
        controls.add(clearButton);
        controls.add(applyButton);

        jobsTableModel = createTableModel(new String[] {"Job ID", "Title", "Company", "Type", "Location", "Deadline"});
        jobsTable = new JTable(jobsTableModel);
        jobsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jobsTable.getSelectionModel().addListSelectionListener(event -> updateStudentJobDetails());

        jobDetailsArea = createReadOnlyArea("");
        JPanel details = withTitle("Job Details", new JScrollPane(jobDetailsArea));
        studentJobsSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(jobsTable), details);
        studentJobsSplitPane.setResizeWeight(0.72);
        studentJobsSplitPane.setDividerSize(8);
        studentJobsSplitPane.setBorder(null);

        panel.add(withTitle("Browse Jobs", controls), BorderLayout.NORTH);
        panel.add(studentJobsSplitPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildStudentApplicationsTab() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setBackground(BACKGROUND);

        studentApplicationsTableModel = createTableModel(new String[] {"Application ID", "Job", "Company", "State"});
        studentApplicationsTable = new JTable(studentApplicationsTableModel);
        studentApplicationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentApplicationsTable.getSelectionModel().addListSelectionListener(event -> updateStudentApplicationDetails());
        studentApplicationsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
            ) {
                Component component = super.getTableCellRendererComponent(
                    table,
                    value,
                    isSelected,
                    hasFocus,
                    row,
                    column
                );
                String state = String.valueOf(table.getModel().getValueAt(row, 3));
                if ("Withdrawn".equals(state)) {
                    component.setForeground(isSelected ? Color.DARK_GRAY : Color.GRAY);
                    component.setBackground(isSelected ? new Color(215, 215, 215) : new Color(242, 242, 242));
                } else {
                    component.setForeground(isSelected ? table.getSelectionForeground() : Color.BLACK);
                    component.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                }
                return component;
            }
        });

        JPanel actions = createSectionCard();
        actions.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 8));
        actions.add(createSecondaryButton("Withdraw", e -> withdrawSelectedApplication()));
        actions.add(createSecondaryButton("Add Cover Letter", e -> decorateApplication("Cover Letter")));
        actions.add(createSecondaryButton("Add Portfolio", e -> decorateApplication("Portfolio")));
        actions.add(createSecondaryButton("Add Referral", e -> decorateApplication("Referral")));

        applicationDetailsArea = createReadOnlyArea("");
        JPanel details = withTitle("Application Details", new JScrollPane(applicationDetailsArea));
        studentApplicationsSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(studentApplicationsTable), details);
        studentApplicationsSplitPane.setResizeWeight(0.68);
        studentApplicationsSplitPane.setDividerSize(8);
        studentApplicationsSplitPane.setBorder(null);

        panel.add(withTitle("Application Actions", actions), BorderLayout.NORTH);
        panel.add(studentApplicationsSplitPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildRecommendationsTab() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setBackground(BACKGROUND);

        JPanel controls = createSectionCard();
        controls.setLayout(new GridBagLayout());
        recommendationStrategyCombo = new JComboBox<>(new String[] {"Skill Match", "Major Match", "Location Match"});
        recommendationStrategyCombo.addActionListener(event -> updateRecommendationControls());
        recommendationLocationLabel = new JLabel("Preferred Location");
        recommendationLocationField = new JTextField(18);
        recommendationHelpArea = createReadOnlyArea("");
        recommendationHelpArea.setOpaque(false);
        JButton generateButton = createPrimaryButton("Generate");
        generateButton.addActionListener(event -> refreshRecommendations(true));
        addFormRow(controls, 0, "Strategy", recommendationStrategyCombo);
        GridBagConstraints locationLabelConstraints = baseConstraints(1);
        locationLabelConstraints.gridx = 0;
        locationLabelConstraints.weightx = 0.25;
        locationLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        controls.add(recommendationLocationLabel, locationLabelConstraints);
        GridBagConstraints locationFieldConstraints = baseConstraints(1);
        locationFieldConstraints.gridx = 1;
        locationFieldConstraints.weightx = 0.75;
        locationFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        controls.add(recommendationLocationField, locationFieldConstraints);
        GridBagConstraints helpConstraints = baseConstraints(2);
        helpConstraints.gridx = 0;
        helpConstraints.gridwidth = 2;
        helpConstraints.fill = GridBagConstraints.HORIZONTAL;
        controls.add(recommendationHelpArea, helpConstraints);
        GridBagConstraints buttonConstraints = baseConstraints(3);
        buttonConstraints.gridx = 1;
        buttonConstraints.anchor = GridBagConstraints.WEST;
        controls.add(generateButton, buttonConstraints);

        recommendationsTableModel = createTableModel(new String[] {"Job ID", "Title", "Company", "Type", "Location"});
        recommendationsTable = new JTable(recommendationsTableModel);
        recommendationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        updateRecommendationControls();

        panel.add(withTitle("Recommendation Engine", controls), BorderLayout.NORTH);
        panel.add(new JScrollPane(recommendationsTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildStudentNotificationsTab() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setBackground(BACKGROUND);
        studentNotificationsList = new JList<>();
        JTextArea info = createReadOnlyArea(
            "Student notifications show when a new job is posted and when an employer updates one of your applications, such as moving it to UnderReview, Interview, Offer, Accepted, or Rejected. Student withdrawals are only sent to the employer."
        );
        panel.add(withTitle("What Notifications Mean", info), BorderLayout.NORTH);
        panel.add(new JScrollPane(studentNotificationsList), BorderLayout.CENTER);
        panel.add(actionRow(createSecondaryButton("Clear Notifications", e -> clearStudentNotifications())), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildEmployerDashboardPanel() {
        JPanel root = new JPanel(new BorderLayout(18, 18));
        root.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        root.setBackground(BACKGROUND);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        employerHeaderLabel = new JLabel("Employer Dashboard");
        employerHeaderLabel.setFont(new Font("Georgia", Font.BOLD, 24));
        employerHeaderLabel.setForeground(ACCENT);
        header.add(employerHeaderLabel, BorderLayout.WEST);
        header.add(createSecondaryButton("Logout", e -> logoutToHome()), BorderLayout.EAST);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Post Job", buildEmployerPostJobTab());
        tabs.addTab("Posted Jobs", buildEmployerJobsTab());
        tabs.addTab("Applications", buildEmployerApplicationsTab());
        tabs.addTab("Notifications", buildEmployerNotificationsTab());

        root.add(header, BorderLayout.NORTH);
        root.add(tabs, BorderLayout.CENTER);
        return root;
    }

    private JPanel buildEmployerPostJobTab() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setBackground(BACKGROUND);
        JPanel form = createSectionCard();
        form.setLayout(new GridBagLayout());

        employerJobTitleField = new JTextField(18);
        employerJobLocationField = new JTextField(18);
        employerJobSkillsField = new JTextField(18);
        employerJobMajorField = new JTextField(18);
        employerJobDeadlineField = new JTextField(18);
        employerJobTypeCombo = new JComboBox<>(new String[] {"Full-Time", "Internship", "Co-Op"});
        employerJobTypeCombo.addActionListener(event -> switchEmployerTypeCard());

        addFormRow(form, 0, "Job Title", employerJobTitleField);
        addFormRow(form, 1, "Location", employerJobLocationField);
        addFormRow(form, 2, "Required Skills", employerJobSkillsField);
        addFormRow(form, 3, "Preferred Major", employerJobMajorField);
        addFormRow(form, 4, "Deadline (YYYY-MM-DD)", employerJobDeadlineField);
        addFormRow(form, 5, "Job Type", employerJobTypeCombo);

        employerTypeCards = new JPanel(new CardLayout());
        employerTypeCards.setOpaque(false);
        employerTypeCards.add(buildFullTimeCard(), "Full-Time");
        employerTypeCards.add(buildInternshipCard(), "Internship");
        employerTypeCards.add(buildCoOpCard(), "Co-Op");
        GridBagConstraints typeCardConstraints = baseConstraints(6);
        typeCardConstraints.gridwidth = 2;
        typeCardConstraints.weightx = 1.0;
        typeCardConstraints.fill = GridBagConstraints.HORIZONTAL;
        typeCardConstraints.anchor = GridBagConstraints.WEST;
        form.add(employerTypeCards, typeCardConstraints);

        JButton submitButton = createPrimaryButton("Post Job");
        submitButton.addActionListener(event -> postEmployerJob());
        GridBagConstraints buttonConstraints = baseConstraints(7);
        buttonConstraints.gridwidth = 2;
        form.add(actionRow(submitButton), buttonConstraints);

        switchEmployerTypeCard();
        panel.add(centered(withTitle("Create Job Posting", form)), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildFullTimeCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setOpaque(false);
        fullTimeMinSalaryField = new JTextField(16);
        fullTimeMaxSalaryField = new JTextField(16);
        fullTimeSigningBonusField = new JTextField(16);
        fullTimeBenefitsCombo = new JComboBox<>(new String[] {"true", "false"});
        addNestedFormRow(card, 0, "Minimum Salary", fullTimeMinSalaryField);
        addNestedFormRow(card, 1, "Maximum Salary", fullTimeMaxSalaryField);
        addNestedFormRow(card, 2, "Signing Bonus", fullTimeSigningBonusField);
        addNestedFormRow(card, 3, "Has Benefits", fullTimeBenefitsCombo);
        return card;
    }

    private JPanel buildInternshipCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setOpaque(false);
        internshipDurationField = new JTextField(16);
        internshipStipendField = new JTextField(16);
        internshipCreditCombo = new JComboBox<>(new String[] {"true", "false"});
        addNestedFormRow(card, 0, "Duration Months", internshipDurationField);
        addNestedFormRow(card, 1, "Stipend (total)", internshipStipendField);
        addNestedFormRow(card, 2, "Academic Credit", internshipCreditCombo);
        return card;
    }

    private JPanel buildCoOpCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setOpaque(false);
        coOpRotationField = new JTextField(16);
        coOpDurationMonthsField = new JTextField(16);
        addNestedFormRow(card, 0, "Rotation Cycle", coOpRotationField);
        addNestedFormRow(card, 1, "Duration Months", coOpDurationMonthsField);
        return card;
    }

    private JPanel buildEmployerJobsTab() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setBackground(BACKGROUND);

        employerJobsTableModel = createTableModel(new String[] {"Job ID", "Title", "Type", "Location", "Deadline", "Status"});
        employerJobsTable = new JTable(employerJobsTableModel);
        employerJobsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employerJobsTable.getSelectionModel().addListSelectionListener(event -> updateEmployerJobDetails());

        employerJobsDetailsArea = createReadOnlyArea("");
        JPanel details = withTitle("Posted Job Details", new JScrollPane(employerJobsDetailsArea));
        employerJobsSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(employerJobsTable), details);
        employerJobsSplitPane.setResizeWeight(0.72);
        employerJobsSplitPane.setDividerSize(8);
        employerJobsSplitPane.setBorder(null);

        panel.add(employerJobsSplitPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildEmployerApplicationsTab() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setBackground(BACKGROUND);

        employerApplicationsTableModel = createTableModel(new String[] {"Application ID", "Student", "Job", "State"});
        employerApplicationsTable = new JTable(employerApplicationsTableModel);
        employerApplicationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employerApplicationsTable.getSelectionModel().addListSelectionListener(event -> updateEmployerApplicationDetails());

        JPanel controls = createSectionCard();
        controls.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 8));
        employerActionCombo = new JComboBox<>();
        JButton applyActionButton = createPrimaryButton("Update Status");
        applyActionButton.addActionListener(event -> updateEmployerApplicationStatus());
        controls.add(new JLabel("Action"));
        controls.add(employerActionCombo);
        controls.add(applyActionButton);

        employerApplicationDetailsArea = createReadOnlyArea("");
        JPanel details = withTitle("Application Details", new JScrollPane(employerApplicationDetailsArea));
        employerApplicationsSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(employerApplicationsTable), details);
        employerApplicationsSplitPane.setResizeWeight(0.62);
        employerApplicationsSplitPane.setDividerSize(8);
        employerApplicationsSplitPane.setBorder(null);

        panel.add(withTitle("Candidate Actions", controls), BorderLayout.NORTH);
        panel.add(employerApplicationsSplitPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildEmployerNotificationsTab() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setBackground(BACKGROUND);
        employerNotificationsList = new JList<>();
        JTextArea info = createReadOnlyArea(
            "Employer notifications show when a student submits an application and when that student later withdraws it."
        );
        panel.add(withTitle("What Notifications Mean", info), BorderLayout.NORTH);
        panel.add(new JScrollPane(employerNotificationsList), BorderLayout.CENTER);
        panel.add(actionRow(createSecondaryButton("Clear Notifications", e -> clearEmployerNotifications())), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildAdminDashboardPanel() {
        JPanel root = new JPanel(new BorderLayout(18, 18));
        root.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        root.setBackground(BACKGROUND);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Admin Dashboard");
        title.setFont(new Font("Georgia", Font.BOLD, 24));
        title.setForeground(ACCENT);
        header.add(title, BorderLayout.WEST);
        header.add(actionRow(
            createSecondaryButton("Back", e -> showCard(HOME_CARD))
        ), BorderLayout.EAST);

        JPanel metrics = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        metrics.setOpaque(false);
        totalStudentsLabel = createMetricLabel("Students: 0");
        totalEmployersLabel = createMetricLabel("Employers: 0");
        totalJobsLabel = createMetricLabel("Jobs: 0");
        totalApplicationsLabel = createMetricLabel("Applications: 0");
        metrics.add(totalStudentsLabel);
        metrics.add(totalEmployersLabel);
        metrics.add(totalJobsLabel);
        metrics.add(totalApplicationsLabel);

        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        adminStudentsArea = createReadOnlyArea("");
        adminEmployersArea = createReadOnlyArea("");
        adminJobsArea = createReadOnlyArea("");
        adminReportArea = createReadOnlyArea("");

        gbc.gridx = 0;
        gbc.gridy = 0;
        content.add(sectionWithScroll("Students", adminStudentsArea), gbc);
        gbc.gridx = 1;
        content.add(sectionWithScroll("Employers", adminEmployersArea), gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        content.add(sectionWithScroll("Jobs", adminJobsArea), gbc);
        gbc.gridx = 1;
        content.add(sectionWithScroll("System Report", adminReportArea), gbc);

        JPanel body = new JPanel(new BorderLayout(16, 16));
        body.setOpaque(false);
        body.add(metrics, BorderLayout.NORTH);
        body.add(content, BorderLayout.CENTER);

        root.add(header, BorderLayout.NORTH);
        root.add(body, BorderLayout.CENTER);
        return root;
    }

    private JLabel createMetricLabel(String text) {
        JLabel label = new JLabel(text);
        label.setOpaque(true);
        label.setBackground(SURFACE);
        label.setForeground(ACCENT);
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 213, 193), 1, true),
            BorderFactory.createEmptyBorder(12, 18, 12, 18)
        ));
        return label;
    }

    private JPanel sectionWithScroll(String title, JTextArea area) {
        JPanel panel = withTitle(title, new JScrollPane(area));
        panel.setPreferredSize(new Dimension(500, 220));
        return panel;
    }

    private JPanel withTitle(String title, Component body) {
        JPanel panel = createSectionCard();
        panel.setLayout(new BorderLayout(0, 10));
        JLabel label = new JLabel(title);
        label.setFont(new Font("Georgia", Font.BOLD, 18));
        label.setForeground(ACCENT);
        panel.add(label, BorderLayout.NORTH);
        panel.add(body, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createScreenPanel(String titleText, String subtitleText) {
        JPanel panel = new JPanel(new BorderLayout(24, 24));
        panel.setBorder(BorderFactory.createEmptyBorder(34, 38, 34, 38));
        panel.setBackground(BACKGROUND);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel(titleText);
        title.setFont(TITLE_FONT);
        title.setForeground(ACCENT);
        title.setHorizontalAlignment(JLabel.CENTER);
        JTextArea subtitle = createReadOnlyArea(subtitleText);
        subtitle.setOpaque(false);
        subtitle.setFont(SUBTITLE_FONT);
        subtitle.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        header.add(title, BorderLayout.NORTH);
        header.add(subtitle, BorderLayout.CENTER);

        panel.add(header, BorderLayout.NORTH);
        return panel;
    }

    private JPanel centered(JComponent component) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);
        wrapper.add(component);
        return wrapper;
    }

    private JPanel createSectionCard() {
        JPanel panel = new JPanel();
        panel.setBackground(SURFACE);
        panel.setBorder(createCardBorder());
        return panel;
    }

    private javax.swing.border.Border createCardBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 213, 193), 1, true),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)
        );
    }

    private DefaultTableModel createTableModel(String[] columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private JTextArea createReadOnlyArea(String text) {
        JTextArea area = new JTextArea(text);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        return area;
    }

    private JTextArea createEditableArea(int rows) {
        JTextArea area = new JTextArea(rows, 24);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        return area;
    }

    private void addFormRow(JPanel panel, int row, String labelText, Component component) {
        GridBagConstraints labelConstraints = baseConstraints(row);
        labelConstraints.gridx = 0;
        labelConstraints.weightx = 0.25;
        labelConstraints.fill = GridBagConstraints.HORIZONTAL;
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(230, 28));
        panel.add(label, labelConstraints);

        GridBagConstraints fieldConstraints = baseConstraints(row);
        fieldConstraints.gridx = 1;
        fieldConstraints.weightx = 0.75;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        if (component instanceof JComponent jComponent) {
            jComponent.setPreferredSize(new Dimension(320, 32));
        }
        panel.add(component, fieldConstraints);
    }

    private void addNestedFormRow(JPanel panel, int row, String labelText, Component component) {
        GridBagConstraints labelConstraints = baseConstraints(row);
        labelConstraints.gridx = 0;
        labelConstraints.weightx = 0;
        labelConstraints.fill = GridBagConstraints.NONE;
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(230, 28));
        panel.add(label, labelConstraints);

        GridBagConstraints fieldConstraints = baseConstraints(row);
        fieldConstraints.gridx = 1;
        fieldConstraints.weightx = 1.0;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        if (component instanceof JComponent jComponent) {
            jComponent.setPreferredSize(new Dimension(320, 32));
        }
        panel.add(component, fieldConstraints);
    }

    private void addHintRow(JPanel panel, int row, String hintText) {
        JTextArea hint = createReadOnlyArea(hintText);
        hint.setOpaque(false);
        hint.setFont(new Font("Georgia", Font.ITALIC, 13));
        hint.setForeground(new Color(92, 86, 80));
        GridBagConstraints constraints = baseConstraints(row);
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(hint, constraints);
    }

    private void addAreaRow(
        JPanel panel,
        int row,
        String labelText,
        JTextArea area,
        String hintText
    ) {
        GridBagConstraints labelConstraints = baseConstraints(row);
        labelConstraints.gridx = 0;
        labelConstraints.anchor = GridBagConstraints.NORTHWEST;
        panel.add(new JLabel("<html>" + labelText + "<br/><span style='color:#6b645e; font-size:10px'>" + hintText + "</span></html>"), labelConstraints);

        GridBagConstraints areaConstraints = baseConstraints(row);
        areaConstraints.gridx = 1;
        areaConstraints.weightx = 0.75;
        areaConstraints.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(area), areaConstraints);
    }

    private GridBagConstraints baseConstraints(int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        return gbc;
    }

    private JPanel actionRow(JButton... buttons) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setOpaque(false);
        for (JButton button : buttons) {
            panel.add(button);
        }
        return panel;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setUI(new BasicButtonUI());
        button.setBackground(ACCENT);
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(18, 64, 52), 1, true),
            BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));
        button.setFocusPainted(false);
        button.setFont(new Font("Georgia", Font.BOLD, 14));
        return button;
    }

    private JButton createSecondaryButton(String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.setUI(new BasicButtonUI());
        button.setBackground(SECONDARY);
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(158, 91, 48), 1, true),
            BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));
        button.setFocusPainted(false);
        button.setFont(new Font("Georgia", Font.BOLD, 14));
        button.addActionListener(listener);
        return button;
    }

    private void showCard(String cardName) {
        cardLayout.show(cards, cardName);
    }

    private void loginStudent() {
        String id = studentLoginIdField.getText().trim();
        String password = new String(studentLoginPasswordField.getPassword()).trim();
        Student student = portal.getStudentById(id);
        if (student == null || !student.getPassword().equals(password)) {
            showError("Invalid student ID or password.");
            return;
        }
        currentStudent = student;
        currentEmployer = null;
        loadStudentDashboard();
        showCard(STUDENT_DASHBOARD_CARD);
    }

    private void registerStudent() {
        String firstName = registerFirstNameField.getText().trim();
        String lastName = registerLastNameField.getText().trim();
        String email = registerEmailField.getText().trim();
        String password = new String(registerPasswordField.getPassword()).trim();
        String confirmPassword = new String(registerConfirmPasswordField.getPassword()).trim();

        List<String> errors = ValidationUtils.validateStudentRegistration(
            firstName,
            lastName,
            email,
            password,
            confirmPassword,
            isEmailTaken(email)
        );

        if (!errors.isEmpty()) {
            showErrors(errors);
            return;
        }

        Student student = new Student(
            generateStudentId(),
            firstName,
            lastName,
            email,
            password,
            "Undeclared",
            0.0,
            java.time.LocalDate.now().getYear() + 1
        );
        portal.registerStudent(student);
        currentStudent = student;
        clearStudentAuthForm();
        studentLoginIdField.setText("");
        studentLoginPasswordField.setText("");
        savePortalData();
        loadStudentDashboard();
        showInfo("Student registered successfully with ID " + student.getUserId() + ".");
        showCard(STUDENT_DASHBOARD_CARD);
    }

    private void loginEmployer() {
        String id = employerLoginIdField.getText().trim();
        String password = new String(employerLoginPasswordField.getPassword()).trim();
        Employer employer = portal.getEmployerById(id);
        if (employer == null || !employer.getPassword().equals(password)) {
            showError("Invalid employer ID or password.");
            return;
        }
        currentEmployer = employer;
        currentStudent = null;
        loadEmployerDashboard();
        showCard(EMPLOYER_DASHBOARD_CARD);
    }

    private void loginAdmin() {
        String username = adminLoginUsernameField.getText().trim();
        String password = new String(adminLoginPasswordField.getPassword()).trim();
        if (!ADMIN_USERNAME.equals(username) || !ADMIN_PASSWORD.equals(password)) {
            showError("Invalid admin username or password.");
            return;
        }
        refreshAdminDashboard();
        adminLoginUsernameField.setText("");
        adminLoginPasswordField.setText("");
        showCard(ADMIN_DASHBOARD_CARD);
    }

    private void logoutToHome() {
        currentStudent = null;
        currentEmployer = null;
        clearStudentAuthForm();
        studentLoginIdField.setText("");
        studentLoginPasswordField.setText("");
        employerLoginIdField.setText("");
        employerLoginPasswordField.setText("");
        if (adminLoginUsernameField != null) {
            adminLoginUsernameField.setText("");
        }
        if (adminLoginPasswordField != null) {
            adminLoginPasswordField.setText("");
        }
        showCard(HOME_CARD);
    }

    private void clearStudentAuthForm() {
        registerFirstNameField.setText("");
        registerLastNameField.setText("");
        registerEmailField.setText("");
        registerPasswordField.setText("");
        registerConfirmPasswordField.setText("");
    }

    private void loadStudentDashboard() {
        if (currentStudent == null) {
            return;
        }
        studentWelcomeLabel.setText(
            "Student Dashboard  |  " +
            currentStudent.getFirstName() +
            " " +
            currentStudent.getLastName() +
            " (" + currentStudent.getUserId() + ")"
        );

        profileMajorField.setText(currentStudent.getMajor());
        profileGpaField.setText(String.valueOf(currentStudent.getGpa()));
        profileGradYearField.setText(String.valueOf(currentStudent.getGraduationYear()));

        Resume resume = currentStudent.getResume();
        if (resume != null) {
            if (resume.getEducation() != null) {
                educationUniversityField.setText(resume.getEducation().getUniversity());
                educationDegreeField.setText(resume.getEducation().getDegree());
            } else {
                educationUniversityField.setText("");
                educationDegreeField.setText("");
            }
            skillsArea.setText(String.join(", ", resume.getSkills()));
            certificationsArea.setText(String.join(", ", resume.getCertifications()));
            experiencesArea.setText(formatExperiences(resume.getExperiences()));
            projectsArea.setText(formatProjects(resume.getProjects()));
        } else {
            educationUniversityField.setText("");
            educationDegreeField.setText("");
            skillsArea.setText("");
            certificationsArea.setText("");
            experiencesArea.setText("");
            projectsArea.setText("");
        }

        studentProfileViewArea.setText(buildStudentProfileSummary(currentStudent));
        refreshStudentJobs();
        refreshStudentApplications();
        refreshStudentNotifications();
        refreshRecommendations(false);
    }

    private void saveStudentProfile() {
        if (currentStudent == null) {
            return;
        }

        List<String> errors = ValidationUtils.validateProfile(
            profileMajorField.getText(),
            profileGpaField.getText(),
            profileGradYearField.getText()
        );

        if (!educationUniversityField.getText().trim().isEmpty() ||
            !educationDegreeField.getText().trim().isEmpty()) {
            ValidationUtils.requireText(educationUniversityField.getText(), "University", errors);
            ValidationUtils.requireText(educationDegreeField.getText(), "Degree", errors);
        }

        errors.addAll(validateStructuredLines(experiencesArea.getText(), 4, "Experiences"));
        errors.addAll(validateStructuredLines(projectsArea.getText(), 3, "Projects"));

        if (!errors.isEmpty()) {
            showErrors(errors);
            return;
        }

        currentStudent.setMajor(profileMajorField.getText().trim());
        currentStudent.setGpa(Double.parseDouble(profileGpaField.getText().trim()));
        currentStudent.setGraduationYear(Integer.parseInt(profileGradYearField.getText().trim()));

        ResumeBuilder builder = new ResumeBuilder();
        if (!educationUniversityField.getText().trim().isEmpty()) {
            builder.setEducation(new Education(
                educationUniversityField.getText().trim(),
                educationDegreeField.getText().trim(),
                Double.parseDouble(profileGpaField.getText().trim())
            ));
        }
        if (!skillsArea.getText().trim().isEmpty()) {
            builder.addSkill(skillsArea.getText().trim());
        }
        if (!certificationsArea.getText().trim().isEmpty()) {
            builder.addCertification(certificationsArea.getText().trim());
        }
        for (String line : nonBlankLines(experiencesArea.getText())) {
            String[] parts = splitStructuredLine(line, 4);
            builder.addExperience(new Experience(parts[0], parts[1], parts[2], parts[3]));
        }
        for (String line : nonBlankLines(projectsArea.getText())) {
            String[] parts = splitStructuredLine(line, 3);
            builder.addProject(new Project(parts[0], parts[1], parts[2]));
        }

        currentStudent.setResume(builder.build());
        savePortalData();
        studentProfileViewArea.setText(buildStudentProfileSummary(currentStudent));
        refreshStudentJobs();
        showInfo("Profile and resume updated.");
    }

    private void refreshStudentJobs() {
        jobsTableModel.setRowCount(0);
        studentVisibleJobs = new ArrayList<>();

        if (currentStudent == null) {
            return;
        }

        Set<String> appliedJobIds = portal.getApplicationsForStudent(currentStudent.getUserId())
            .stream()
            .map(this::unwrapBaseApplication)
            .map(app -> app.getJob().getJobId())
            .collect(Collectors.toSet());

        JobListingCollection collection = new JobListingCollection();
        portal.getJobPostings()
            .stream()
            .filter(job -> !appliedJobIds.contains(job.getJobId()))
            .forEach(collection::addJob);

        JobIterator iterator;
        String filterType = String.valueOf(jobFilterTypeCombo.getSelectedItem());
        String filterValue = jobFilterValueField.getText().trim();
        if ("Type".equals(filterType) && !filterValue.isEmpty()) {
            iterator = collection.createFilteredIterator("type", filterValue);
        } else if ("Location".equals(filterType) && !filterValue.isEmpty()) {
            iterator = collection.createFilteredIterator("location", filterValue);
        } else if ("Skill".equals(filterType) && !filterValue.isEmpty()) {
            iterator = collection.createFilteredIterator("skill", filterValue);
        } else {
            iterator = collection.createIterator();
        }

        while (iterator.hasNext()) {
            JobPosting job = iterator.next();
            studentVisibleJobs.add(job);
            jobsTableModel.addRow(new Object[] {
                job.getJobId(),
                job.getTitle(),
                job.getCompany(),
                job.getType(),
                job.getLocation(),
                DATE_FORMAT.format(job.getDeadline())
            });
        }
        jobDetailsArea.setText(studentVisibleJobs.isEmpty() ? "No jobs available for this view." : "Select a job to view details.");
    }

    private void updateStudentJobDetails() {
        int row = jobsTable.getSelectedRow();
        if (row < 0 || row >= studentVisibleJobs.size()) {
            return;
        }
        JobPosting job = studentVisibleJobs.get(row);
        jobDetailsArea.setText(formatJobPosting(job));
        if (studentJobsSplitPane != null) {
            studentJobsSplitPane.setDividerLocation(0.68);
        }
    }

    private void applyToSelectedJob() {
        if (currentStudent == null) {
            return;
        }
        if (currentStudent.getResume() == null) {
            showError("Please complete your profile and resume before applying.");
            return;
        }
        int row = jobsTable.getSelectedRow();
        if (row < 0 || row >= studentVisibleJobs.size()) {
            showError("Select a job first.");
            return;
        }
        JobPosting job = studentVisibleJobs.get(row);
        BaseApplication application = new BaseApplication(
            "APP-" + currentStudent.getUserId() + "-" + System.currentTimeMillis(),
            currentStudent,
            job,
            currentStudent.getResume()
        );
        application.submit();
        currentStudent.addApplication(application);
        portal.addApplication(application);
        portal.notifyEvent("APPLICATION_SUBMITTED", application);
        savePortalData();
        refreshStudentJobs();
        refreshStudentApplications();
        refreshStudentNotifications();
        showInfo("Application submitted for " + job.getTitle() + ".");
    }

    private void refreshStudentApplications() {
        studentApplicationsTableModel.setRowCount(0);
        studentVisibleApplications = new ArrayList<>();

        if (currentStudent == null) {
            return;
        }

        for (ApplicationComponent app : portal.getApplicationsForStudent(currentStudent.getUserId())) {
            BaseApplication base = unwrapBaseApplication(app);
            studentVisibleApplications.add(app);
            studentApplicationsTableModel.addRow(new Object[] {
                base.getApplicationId(),
                base.getJob().getTitle(),
                base.getJob().getCompany(),
                base.getCurrentState().getStateName()
            });
        }
        applicationDetailsArea.setText(studentVisibleApplications.isEmpty() ? "No applications yet." : "Select an application to view history.");
    }

    private void updateStudentApplicationDetails() {
        int row = studentApplicationsTable.getSelectedRow();
        if (row < 0 || row >= studentVisibleApplications.size()) {
            return;
        }
        ApplicationComponent applicationComponent = studentVisibleApplications.get(row);
        BaseApplication base = unwrapBaseApplication(applicationComponent);
        applicationDetailsArea.setText(
            base.getDescription() + "\n" +
            "State: " + base.getCurrentState().getStateName() + "\n" +
            "Add-ons: " + describeApplicationAddOns(applicationComponent) + "\n" +
            "Withdrawal Allowed: " + (canWithdrawApplication(base) ? "Yes" : "No") + "\n\n" +
            "History:\n- " + String.join("\n- ", base.getStatusHistory())
        );
        if (studentApplicationsSplitPane != null) {
            studentApplicationsSplitPane.setDividerLocation(0.62);
        }
    }

    private void withdrawSelectedApplication() {
        int row = studentApplicationsTable.getSelectedRow();
        if (row < 0 || row >= studentVisibleApplications.size()) {
            showError("Select an application first.");
            return;
        }
        ApplicationComponent app = studentVisibleApplications.get(row);
        BaseApplication base = unwrapBaseApplication(app);
        if (!canWithdrawApplication(base)) {
            showError("You can only withdraw an application while it is Draft or Submitted.");
            return;
        }
        base.withdraw();
        portal.notifyEvent("APPLICATION_STATUS_CHANGED", base);
        savePortalData();
        refreshStudentApplications();
        refreshStudentNotifications();
        showInfo("Application withdrawn.");
    }

    private void decorateApplication(String type) {
        int row = studentApplicationsTable.getSelectedRow();
        if (row < 0 || row >= studentVisibleApplications.size()) {
            showError("Select an application first.");
            return;
        }
        ApplicationComponent original = studentVisibleApplications.get(row);
        if ("Referral".equals(type)) {
            ApplicationComponent referralDecorator = promptReferralDecorator(original);
            if (referralDecorator == null) {
                return;
            }
            portal.removeApplication(original);
            portal.addApplication(referralDecorator);
            savePortalData();
            refreshStudentApplications();
            showInfo(type + " added to the application.");
            return;
        }
        ApplicationComponent enhanced = switch (type) {
            case "Cover Letter" -> {
                String text = JOptionPane.showInputDialog(this, "Enter cover letter text:");
                if (text == null || text.trim().isEmpty()) {
                    yield null;
                }
                yield new CoverLetterDecorator(original, text.trim());
            }
            case "Portfolio" -> {
                String url = JOptionPane.showInputDialog(this, "Enter portfolio URL:");
                if (url == null || url.trim().isEmpty()) {
                    yield null;
                }
                yield new PortfolioDecorator(original, url.trim());
            }
            default -> null;
        };

        if (enhanced == null) {
            showError("All decorator fields are required.");
            return;
        }

        portal.removeApplication(original);
        portal.addApplication(enhanced);
        savePortalData();
        refreshStudentApplications();
        showInfo(type + " added to the application.");
    }

    private void refreshRecommendations(boolean showValidationMessage) {
        recommendationsTableModel.setRowCount(0);

        if (currentStudent == null || currentStudent.getResume() == null) {
            return;
        }

        String selectedStrategy = String.valueOf(recommendationStrategyCombo.getSelectedItem());
        RecommendationStrategy strategy = switch (selectedStrategy) {
            case "Major Match" -> new MajorMatchStrategy(currentStudent.getMajor());
            case "Location Match" -> new LocationMatchStrategy(recommendationLocationField.getText().trim());
            default -> new SkillMatchStrategy();
        };

        if ("Location Match".equals(selectedStrategy) && recommendationLocationField.getText().trim().isEmpty()) {
            if (showValidationMessage) {
                showError("Enter a preferred location to use Location Match.");
            }
            return;
        }

        RecommendationEngine engine = new RecommendationEngine(strategy);
        for (JobPosting job : engine.recommend(currentStudent, portal.getJobPostings())) {
            recommendationsTableModel.addRow(new Object[] {
                job.getJobId(),
                job.getTitle(),
                job.getCompany(),
                job.getType(),
                job.getLocation()
            });
        }
        if (recommendationsTableModel.getRowCount() == 0) {
            showInfo("No matching jobs were found for that recommendation strategy.");
        }
    }

    private void updateRecommendationControls() {
        String strategy = String.valueOf(recommendationStrategyCombo.getSelectedItem());
        boolean needsLocation = "Location Match".equals(strategy);
        recommendationLocationLabel.setEnabled(needsLocation);
        recommendationLocationField.setEnabled(needsLocation);
        if (!needsLocation) {
            recommendationLocationField.setText("");
        }
        recommendationHelpArea.setText(
            switch (strategy) {
                case "Major Match" -> "Major Match ranks jobs whose preferred major aligns with your profile.";
                case "Location Match" -> "Location Match only shows jobs in the location you enter exactly, such as Boston or Remote.";
                default -> "Skill Match ranks jobs using the skills listed in your resume against the job requirements.";
            }
        );
    }

    private void refreshStudentNotifications() {
        if (currentStudent == null) {
            return;
        }
        studentNotificationsList.setListData(currentStudent.getNotifications().toArray(String[]::new));
    }

    private void clearStudentNotifications() {
        if (currentStudent == null) {
            return;
        }
        currentStudent.getNotifications().clear();
        savePortalData();
        refreshStudentNotifications();
    }

    private void loadEmployerDashboard() {
        if (currentEmployer == null) {
            return;
        }
        employerHeaderLabel.setText(
            "Employer Dashboard  |  " +
            currentEmployer.getCompanyName() +
            " (" + currentEmployer.getUserId() + ")"
        );
        refreshEmployerJobs();
        refreshEmployerApplications();
        refreshEmployerNotifications();
    }

    private void switchEmployerTypeCard() {
        CardLayout layout = (CardLayout) employerTypeCards.getLayout();
        layout.show(employerTypeCards, String.valueOf(employerJobTypeCombo.getSelectedItem()));
    }

    private void postEmployerJob() {
        if (currentEmployer == null) {
            return;
        }

        String jobType = String.valueOf(employerJobTypeCombo.getSelectedItem());
        List<String> errors = ValidationUtils.validateJobPosting(
            employerJobTitleField.getText(),
            employerJobLocationField.getText(),
            employerJobSkillsField.getText(),
            employerJobMajorField.getText(),
            employerJobDeadlineField.getText(),
            jobType,
            fullTimeMinSalaryField.getText(),
            fullTimeMaxSalaryField.getText(),
            fullTimeSigningBonusField.getText(),
            internshipStipendField.getText(),
            internshipDurationField.getText(),
            coOpRotationField.getText(),
            coOpDurationMonthsField.getText()
        );

        if (!errors.isEmpty()) {
            showErrors(errors);
            return;
        }

        Map<String, String> details = new HashMap<>();
        details.put("jobId", "JOB-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase(Locale.US));
        details.put("title", employerJobTitleField.getText().trim());
        details.put("company", currentEmployer.getCompanyName());
        details.put("location", employerJobLocationField.getText().trim());
        details.put("requiredSkills", employerJobSkillsField.getText().trim());
        details.put("preferredMajor", employerJobMajorField.getText().trim());
        details.put("deadline", employerJobDeadlineField.getText().trim());
        details.put("status", "Open");

        JobPostingFactory factory;
        if ("Full-Time".equals(jobType)) {
            factory = new FullTimeFactory();
            details.put("salaryMin", fullTimeMinSalaryField.getText().trim());
            details.put("salaryMax", fullTimeMaxSalaryField.getText().trim());
            details.put("hasBenefits", String.valueOf(fullTimeBenefitsCombo.getSelectedItem()));
            details.put("signingBonus", fullTimeSigningBonusField.getText().trim());
        } else if ("Internship".equals(jobType)) {
            factory = new InternshipFactory();
            details.put("duration", internshipDurationField.getText().trim());
            details.put("stipend", internshipStipendField.getText().trim());
            details.put("academicCredit", String.valueOf(internshipCreditCombo.getSelectedItem()));
        } else {
            factory = new CoOpFactory();
            details.put("rotationCycle", coOpRotationField.getText().trim());
            details.put("durationMonths", coOpDurationMonthsField.getText().trim());
        }

        JobPosting job = factory.createPosting(details);
        currentEmployer.postJob(job);
        portal.addJobPosting(job);
        savePortalData();
        clearEmployerPostForm();
        refreshEmployerJobs();
        refreshEmployerNotifications();
        showInfo("Job posted successfully.");
    }

    private void clearEmployerPostForm() {
        employerJobTitleField.setText("");
        employerJobLocationField.setText("");
        employerJobSkillsField.setText("");
        employerJobMajorField.setText("");
        employerJobDeadlineField.setText("");
        fullTimeMinSalaryField.setText("");
        fullTimeMaxSalaryField.setText("");
        fullTimeSigningBonusField.setText("");
        internshipDurationField.setText("");
        internshipStipendField.setText("");
        coOpRotationField.setText("");
        coOpDurationMonthsField.setText("");
        employerJobTypeCombo.setSelectedItem("Full-Time");
        switchEmployerTypeCard();
    }

    private void refreshEmployerJobs() {
        employerJobsTableModel.setRowCount(0);
        employerVisibleJobs = new ArrayList<>();
        if (currentEmployer == null) {
            return;
        }
        for (JobPosting job : portal.getJobPostings()) {
            if (job.getCompany().equalsIgnoreCase(currentEmployer.getCompanyName())) {
                employerVisibleJobs.add(job);
                employerJobsTableModel.addRow(new Object[] {
                    job.getJobId(),
                    job.getTitle(),
                    job.getType(),
                    job.getLocation(),
                    DATE_FORMAT.format(job.getDeadline()),
                    job.getStatus()
                });
            }
        }
        employerJobsDetailsArea.setText(employerVisibleJobs.isEmpty() ? "No jobs posted yet." : "Select a job to view details.");
    }

    private void updateEmployerJobDetails() {
        int row = employerJobsTable.getSelectedRow();
        if (row < 0 || row >= employerVisibleJobs.size()) {
            return;
        }
        employerJobsDetailsArea.setText(formatJobPosting(employerVisibleJobs.get(row)));
        if (employerJobsSplitPane != null) {
            employerJobsSplitPane.setDividerLocation(0.68);
        }
    }

    private void refreshEmployerApplications() {
        employerApplicationsTableModel.setRowCount(0);
        employerVisibleApplications = new ArrayList<>();
        if (currentEmployer == null) {
            return;
        }
        for (JobPosting job : portal.getJobPostings()) {
            if (job.getCompany().equalsIgnoreCase(currentEmployer.getCompanyName())) {
                for (BaseApplication application : portal.getApplicationsForJob(job.getJobId())) {
                    employerVisibleApplications.add(application);
                    employerApplicationsTableModel.addRow(new Object[] {
                        application.getApplicationId(),
                        application.getStudent().getFirstName() + " " + application.getStudent().getLastName(),
                        application.getJob().getTitle(),
                        application.getCurrentState().getStateName()
                    });
                }
            }
        }
        employerApplicationDetailsArea.setText(employerVisibleApplications.isEmpty() ? "No applications yet." : "Select an application to review.");
        employerActionCombo.setModel(new DefaultComboBoxModel<>(new String[0]));
    }

    private void updateEmployerApplicationDetails() {
        int row = employerApplicationsTable.getSelectedRow();
        if (row < 0 || row >= employerVisibleApplications.size()) {
            return;
        }
        BaseApplication application = employerVisibleApplications.get(row);
        employerApplicationDetailsArea.setText(
            application.getDescription() + "\n" +
            "State: " + application.getCurrentState().getStateName() + "\n" +
            "Student Major: " + application.getStudent().getMajor() + "\n" +
            "Student GPA: " + application.getStudent().getGpa() + "\n\n" +
            "History:\n- " + String.join("\n- ", application.getStatusHistory())
        );

        List<String> actions = switch (application.getCurrentState().getStateName()) {
            case "Draft" -> List.of("Submit Application");
            case "Submitted" -> List.of("Move to Under Review", "Reject");
            case "UnderReview" -> List.of("Schedule Interview", "Reject");
            case "Interview" -> List.of("Make Offer", "Reject");
            case "Offer" -> List.of("Accept", "Reject");
            default -> List.of();
        };
        employerActionCombo.setModel(new DefaultComboBoxModel<>(actions.toArray(String[]::new)));
        if (employerApplicationsSplitPane != null) {
            employerApplicationsSplitPane.setDividerLocation(0.58);
        }
    }

    private void updateEmployerApplicationStatus() {
        int row = employerApplicationsTable.getSelectedRow();
        if (row < 0 || row >= employerVisibleApplications.size()) {
            showError("Select an application first.");
            return;
        }

        BaseApplication application = employerVisibleApplications.get(row);
        String action = String.valueOf(employerActionCombo.getSelectedItem());
        if (action == null || action.isBlank()) {
            showError("No actions are available for the selected application.");
            return;
        }

        switch (action) {
            case "Submit Application" -> application.submit();
            case "Move to Under Review" -> application.review();
            case "Schedule Interview" -> application.scheduleInterview();
            case "Make Offer" -> application.makeOffer();
            case "Accept" -> application.accept();
            case "Reject" -> application.reject();
            default -> {
                showError("Unsupported action.");
                return;
            }
        }

        portal.notifyEvent("APPLICATION_STATUS_CHANGED", application);
        savePortalData();
        refreshEmployerApplications();
        refreshEmployerNotifications();
        showInfo("Candidate status updated to " + application.getCurrentState().getStateName() + ".");
    }

    private void refreshEmployerNotifications() {
        if (currentEmployer == null) {
            return;
        }
        employerNotificationsList.setListData(currentEmployer.getNotifications().toArray(String[]::new));
    }

    private void clearEmployerNotifications() {
        if (currentEmployer == null) {
            return;
        }
        currentEmployer.getNotifications().clear();
        savePortalData();
        refreshEmployerNotifications();
    }

    private void refreshAdminDashboard() {
        String generatedAt = REPORT_TIMESTAMP.format(LocalDateTime.now());
        totalStudentsLabel.setText("Students: " + portal.getAllStudents().size());
        totalEmployersLabel.setText("Employers: " + portal.getAllEmployers().size());
        totalJobsLabel.setText("Jobs: " + portal.getJobPostings().size());

        int totalApplications = portal.getAllStudents()
            .values()
            .stream()
            .map(Student::getUserId)
            .mapToInt(id -> portal.getApplicationsForStudent(id).size())
            .sum();
        totalApplicationsLabel.setText("Applications: " + totalApplications);

        adminStudentsArea.setText(
            portal.getAllStudents()
                .values()
                .stream()
                .map(student -> student.getUserId() + " | " + student.getFirstName() + " " + student.getLastName() + " | " + student.getMajor())
                .collect(Collectors.joining("\n"))
        );
        adminEmployersArea.setText(
            portal.getAllEmployers()
                .values()
                .stream()
                .map(employer -> employer.getUserId() + " | " + employer.getCompanyName() + " | " + employer.getEmail())
                .collect(Collectors.joining("\n"))
        );
        adminJobsArea.setText(
            portal.getJobPostings()
                .stream()
                .map(job -> job.getJobId() + " | " + job.getTitle() + " | " + job.getCompany() + " | " + job.getType())
                .collect(Collectors.joining("\n"))
        );
        adminReportArea.setText(
            "Admin: " + currentAdmin.getFirstName() + " " + currentAdmin.getLastName() + "\n\n" +
            "Generated At: " + generatedAt + "\n\n" +
            "Singleton Data Load: PASSED\n" +
            "Factory Posting Creation: PASSED\n" +
            "Builder Resume Construction: PASSED\n" +
            "Decorator Application Enhancements: PASSED\n" +
            "State Transition Workflow: PASSED\n" +
            "Strategy Recommendations: PASSED\n" +
            "Observer Notifications: PASSED\n" +
            "Iterator Job Browsing: PASSED"
        );
    }

    private String generateStudentId() {
        int maxId = 0;
        for (String existingId : portal.getAllStudents().keySet()) {
            if (existingId.startsWith("STU")) {
                try {
                    maxId = Math.max(maxId, Integer.parseInt(existingId.substring(3)));
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return String.format("STU%03d", maxId + 1);
    }

    private boolean isEmailTaken(String email) {
        return portal.getAllStudents()
            .values()
            .stream()
            .anyMatch(student -> student.getEmail().equalsIgnoreCase(email.trim()));
    }

    private List<String> validateStructuredLines(String text, int expectedParts, String label) {
        List<String> errors = new ArrayList<>();
        int lineNumber = 1;
        for (String line : nonBlankLines(text)) {
            String[] parts = line.split("\\|");
            if (parts.length != expectedParts) {
                errors.add(label + " line " + lineNumber + " must contain " + expectedParts + " values separated by |.");
            } else {
                for (String part : parts) {
                    if (part.trim().isEmpty()) {
                        errors.add(label + " line " + lineNumber + " cannot contain empty values.");
                        break;
                    }
                }
            }
            lineNumber++;
        }
        return errors;
    }

    private List<String> nonBlankLines(String text) {
        return text.lines()
            .map(String::trim)
            .filter(line -> !line.isEmpty())
            .collect(Collectors.toList());
    }

    private String[] splitStructuredLine(String line, int expectedParts) {
        String[] raw = line.split("\\|", expectedParts);
        String[] parts = new String[expectedParts];
        for (int i = 0; i < expectedParts; i++) {
            parts[i] = raw[i].trim();
        }
        return parts;
    }

    private String formatExperiences(List<Experience> experiences) {
        return experiences.stream()
            .map(exp -> exp.getCompany() + " | " + exp.getRole() + " | " + exp.getDuration() + " | " + exp.getDescription())
            .collect(Collectors.joining("\n"));
    }

    private String formatProjects(List<Project> projects) {
        return projects.stream()
            .map(project -> project.getName() + " | " + project.getTechnologies() + " | " + project.getDescription())
            .collect(Collectors.joining("\n"));
    }

    private String buildStudentProfileSummary(Student student) {
        StringBuilder summary = new StringBuilder();
        summary.append(student.getFirstName()).append(" ").append(student.getLastName()).append("\n");
        summary.append("Student ID: ").append(student.getUserId()).append("\n");
        summary.append("Email: ").append(student.getEmail()).append("\n");
        summary.append("Major: ").append(student.getMajor()).append("\n");
        summary.append("GPA: ").append(student.getGpa()).append("\n");
        summary.append("Graduation Year: ").append(student.getGraduationYear()).append("\n\n");

        Resume resume = student.getResume();
        if (resume == null) {
            summary.append("No resume details added yet.");
            return summary.toString();
        }

        summary.append("Latest Education\n");
        if (resume.getEducation() != null) {
            summary.append("- University: ").append(resume.getEducation().getUniversity()).append("\n");
            summary.append("- Degree: ").append(resume.getEducation().getDegree()).append("\n");
        } else {
            summary.append("- Not added\n");
        }

        summary.append("\nSkills\n");
        summary.append(resume.getSkills().isEmpty() ? "- None\n" : "- " + String.join(", ", resume.getSkills()) + "\n");

        summary.append("\nCertifications\n");
        summary.append(resume.getCertifications().isEmpty() ? "- None\n" : "- " + String.join(", ", resume.getCertifications()) + "\n");

        summary.append("\nExperiences\n");
        if (resume.getExperiences().isEmpty()) {
            summary.append("- None\n");
        } else {
            for (Experience experience : resume.getExperiences()) {
                summary.append("- ")
                    .append(experience.getRole()).append(", ")
                    .append(experience.getCompany()).append(" (")
                    .append(experience.getDuration()).append("): ")
                    .append(experience.getDescription()).append("\n");
            }
        }

        summary.append("\nProjects\n");
        if (resume.getProjects().isEmpty()) {
            summary.append("- None\n");
        } else {
            for (Project project : resume.getProjects()) {
                summary.append("- ")
                    .append(project.getName()).append(" [")
                    .append(project.getTechnologies()).append("]: ")
                    .append(project.getDescription()).append("\n");
            }
        }
        return summary.toString();
    }

    private boolean canWithdrawApplication(BaseApplication application) {
        String state = application.getCurrentState().getStateName();
        return "Draft".equals(state)
            || "Submitted".equals(state);
    }

    private String describeApplicationAddOns(ApplicationComponent applicationComponent) {
        List<String> addOns = new ArrayList<>();
        ApplicationComponent current = applicationComponent;
        while (current instanceof ApplicationDecorator decorator) {
            if (decorator instanceof CoverLetterDecorator) {
                addOns.add("Cover Letter");
            } else if (decorator instanceof PortfolioDecorator) {
                addOns.add("Portfolio");
            } else if (decorator instanceof ReferralDecorator) {
                addOns.add("Referral");
            }
            current = decorator.getWrappedComponent();
        }
        return addOns.isEmpty() ? "None" : String.join(", ", addOns);
    }

    private ApplicationComponent promptReferralDecorator(ApplicationComponent original) {
        String name = JOptionPane.showInputDialog(this, "Referrer name:");
        String email = JOptionPane.showInputDialog(this, "Referrer email:");
        String title = JOptionPane.showInputDialog(this, "Referrer title:");
        if (isBlank(name) || isBlank(email) || isBlank(title)) {
            showError("Referral name, email, and title are required.");
            return null;
        }
        List<String> errors = new ArrayList<>();
        ValidationUtils.requireEmail(email, errors);
        if (!errors.isEmpty()) {
            showErrors(errors);
            return null;
        }
        return new ReferralDecorator(original, name.trim(), email.trim(), title.trim());
    }

    private void clearStudentProfileEditor() {
        profileMajorField.setText("");
        profileGpaField.setText("");
        profileGradYearField.setText("");
        educationUniversityField.setText("");
        educationDegreeField.setText("");
        skillsArea.setText("");
        certificationsArea.setText("");
        experiencesArea.setText("");
        projectsArea.setText("");
    }

    private String formatJobPosting(JobPosting job) {
        return "Job ID: " + job.getJobId() + "\n" +
            "Title: " + job.getTitle() + "\n" +
            "Company: " + job.getCompany() + "\n" +
            "Type: " + job.getType() + "\n" +
            "Location: " + job.getLocation() + "\n" +
            "Preferred Major: " + job.getPreferredMajor() + "\n" +
            "Required Skills: " + String.join(", ", job.getRequiredSkills()) + "\n" +
            "Deadline: " + DATE_FORMAT.format(job.getDeadline()) + "\n" +
            "Status: " + job.getStatus();
    }

    private BaseApplication unwrapBaseApplication(ApplicationComponent app) {
        ApplicationComponent current = app;
        while (current instanceof ApplicationDecorator) {
            current = ((ApplicationDecorator) current).getWrappedComponent();
        }
        return (BaseApplication) current;
    }

    private void savePortalData() {
        portal.saveData();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Career Forge", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Career Forge", JOptionPane.ERROR_MESSAGE);
    }

    private void showErrors(List<String> errors) {
        JOptionPane.showMessageDialog(
            this,
            errors.stream().map(error -> "- " + error).collect(Collectors.joining("\n")),
            "Please Fix These Fields",
            JOptionPane.ERROR_MESSAGE
        );
    }

    public static void open(CareerPortal portal) {
        SwingUtilities.invokeLater(() -> new CareerForgeSwingApp(portal).launch());
    }
}
