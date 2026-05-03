package com.careerforge.portal;

import com.careerforge.user.Employer;
import com.careerforge.user.Student;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import com.careerforge.job.*;
import com.careerforge.notification.EmployerNotificationListener;
import com.careerforge.notification.EventListener;
import com.careerforge.notification.EventManager;
import com.careerforge.notification.StudentNotificationListener;
import com.careerforge.application.*;

public class CareerPortal {

    private static CareerPortal instance;

    private Map<String, Student> studentMap;
    private Map<String, Employer> employerMap;
    private List<JobPosting> jobPostings;
    private List<ApplicationComponent> applications;
    private EventManager eventManager;

    private CareerPortal() {
        this.studentMap = new HashMap<>();
        this.employerMap = new HashMap<>();
        this.jobPostings = new ArrayList<>();
        this.applications = new ArrayList<>();

        this.eventManager = new EventManager(
            "APPLICATION_SUBMITTED",
            "APPLICATION_STATUS_CHANGED",
            "JOB_POSTED",
            "SYSTEM_ALERT"
        );
    }

    public static CareerPortal getInstance() {
        if (instance == null) {
            instance = new CareerPortal();
        }
        return instance;
    }

    public void registerStudent(Student student) {
        studentMap.put(student.getUserId(), student);
        eventManager.subscribe("APPLICATION_STATUS_CHANGED", new StudentNotificationListener(student));
        eventManager.subscribe("JOB_POSTED", new StudentNotificationListener(student));

    }

    public void registerEmployer(Employer employer) {
        employerMap.put(employer.getUserId(), employer);
        eventManager.subscribe("APPLICATION_SUBMITTED", new EmployerNotificationListener(employer));
        eventManager.subscribe("APPLICATION_STATUS_CHANGED", new EmployerNotificationListener(employer));
    }

    public void subscribe(String eventType, EventListener listener) {
        eventManager.subscribe(eventType, listener);
    }

    public void notifyEvent(String eventType, BaseApplication app) {
        eventManager.notify(eventType, app);
    }

    public void addJobPosting(JobPosting job) {
        jobPostings.add(job);
        eventManager.notify("JOB_POSTED", job);
    }

    public void addApplication(ApplicationComponent application) {
        applications.add(application);
    }

    public List<ApplicationComponent> getApplicationsForStudent(
        String studentId
    ) {
        return applications
            .stream()
            .filter(app ->
                unwrapBase(app).getStudent().getUserId().equals(studentId)
            )
            .collect(Collectors.toList());
    }

    public List<BaseApplication> getApplicationsForJob(String jobId) {
        return applications
            .stream()
            .map(this::unwrapBase)
            .filter(app -> app.getJob().getJobId().equals(jobId))
            .collect(Collectors.toList());
    }

    private BaseApplication unwrapBase(ApplicationComponent app) {
        while (app instanceof ApplicationDecorator) {
            app = ((ApplicationDecorator) app).getWrappedComponent();
        }
        return (BaseApplication) app;
    }

    public void removeApplication(ApplicationComponent app) {
        applications.remove(app);
    }

    public List<JobPosting> getJobPostings() {
        return jobPostings;
    }

    public Student getStudentById(String id) {
        return studentMap.get(id);
    }

    public Employer getEmployerById(String id) {
        return employerMap.get(id);
    }

    public Map<String, Student> getAllStudents() {
        return studentMap;
    }

    public Map<String, Employer> getAllEmployers() {
        return employerMap;
    }

    private Gson buildGson() {
        JsonDeserializer<JobPosting> jobDeserializer = (json, typeOfT, context) -> {
            JsonObject obj = json.getAsJsonObject();
            if (obj.has("salaryMin"))     return context.deserialize(obj, FullTimePosting.class);
            if (obj.has("stipend"))       return context.deserialize(obj, InternshipPosting.class);
            if (obj.has("rotationCycle")) return context.deserialize(obj, CoOpPosting.class);
            throw new JsonParseException("Unknown JobPosting type in: " + obj);
        };

        TypeAdapter<ApplicationState> stateAdapter = new TypeAdapter<ApplicationState>() {
            @Override
            public void write(JsonWriter out, ApplicationState value) throws IOException {
                out.beginObject();
                out.name("stateName");
                out.value(value != null ? value.getStateName() : "Draft");
                out.endObject();
            }

            @Override
            public ApplicationState read(JsonReader in) throws IOException {
                String stateName = "Draft";
                in.beginObject();
                while (in.hasNext()) {
                    String key = in.nextName();
                    if (key.equals("stateName")) {
                        stateName = in.nextString();
                    } else {
                        in.skipValue();
                    }
                }
                in.endObject();
                return stateFromName(stateName);
            }
        };

        TypeAdapter<Date> dateAdapter = new TypeAdapter<Date>() {
            private final SimpleDateFormat sdf =
                new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            @Override
            public void write(JsonWriter out, Date value) throws IOException {
                out.value(value != null ? sdf.format(value) : null);
            }

            @Override
            public Date read(JsonReader in) throws IOException {
                String raw = in.nextString();
                try {
                    return sdf.parse(raw);
                } catch (ParseException e) {
                    throw new IOException("Failed to parse date: " + raw, e);
                }
            }
        };

        return new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Date.class, dateAdapter)
            .registerTypeAdapter(JobPosting.class, jobDeserializer)
            .registerTypeAdapter(ApplicationState.class, stateAdapter)
            .create();
    }

    private ApplicationState stateFromName(String name) {
        switch (name) {
            case "Submitted":   return new SubmittedState();
            case "UnderReview": return new UnderReviewState();
            case "Interview":   return new InterviewState();
            case "Offer":       return new OfferState();
            case "Accepted":    return new AcceptedState();
            case "Rejected":    return new RejectedState();
            case "Withdrawn":   return new WithdrawnState();
            default:            return new DraftState();
        }
    }

    public void loadData() {
        System.out.println("Loading system state from JSON files in data/...");
        Gson gson = buildGson();

        try (Reader reader = new FileReader("data/employers.json")) {
            Type employerListType = new TypeToken<List<Employer>>() {}.getType();
            List<Employer> employers = gson.fromJson(reader, employerListType);
            if (employers != null) {
                for (Employer e : employers) {
                    registerEmployer(e);
                }
            }
        } catch (Exception e) {
            System.out.println("No employers loaded: " + e.getMessage());
        }

        try (Reader reader = new FileReader("data/students.json")) {
            Type studentListType = new TypeToken<List<Student>>() {}.getType();
            List<Student> students = gson.fromJson(reader, studentListType);
            if (students != null) {
                for (Student s : students) {
                    registerStudent(s);
                }
            }
        } catch (Exception e) {
            System.out.println("No students loaded: " + e.getMessage());
        }

        try (Reader reader = new FileReader("data/jobpostings.json")) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            for (JsonElement element : jsonArray) {
                JsonObject obj = element.getAsJsonObject();
                JobPosting job = null;
                if (obj.has("salaryMin"))      job = gson.fromJson(obj, FullTimePosting.class);
                else if (obj.has("stipend"))   job = gson.fromJson(obj, InternshipPosting.class);
                else if (obj.has("rotationCycle")) job = gson.fromJson(obj, CoOpPosting.class);
                if (job != null) addJobPosting(job);
            }
        } catch (Exception e) {
            System.out.println("No jobs loaded: " + e.getMessage());
        }

        try (Reader reader = new FileReader("data/applications.json")) {
            Type appListType = new TypeToken<List<BaseApplication>>() {}.getType();
            List<BaseApplication> apps = gson.fromJson(reader, appListType);
            if (apps != null) {
                for (BaseApplication app : apps) {
                    String studentId = app.getStudent().getUserId();
                    Student actualStudent = studentMap.get(studentId);
                    if (actualStudent != null) {
                        app.setStudent(actualStudent);
                        actualStudent.addApplication(app);
                    }
                    addApplication(app);
                }
            }
        } catch (Exception e) {
            System.out.println("No applications loaded: " + e.getMessage());
        }
    }

    public void saveData() {
        System.out.println("Saving system state to JSON files in data/...");
        Gson gson = buildGson();

        try {
            try (Writer writer = new FileWriter("data/employers.json")) {
                gson.toJson(new ArrayList<>(employerMap.values()), writer);
            }
            try (Writer writer = new FileWriter("data/students.json")) {
                gson.toJson(new ArrayList<>(studentMap.values()), writer);
            }
            try (Writer writer = new FileWriter("data/jobpostings.json")) {
                gson.toJson(jobPostings, writer);
            }
            try (Writer writer = new FileWriter("data/applications.json")) {
                List<BaseApplication> baseApps = applications
                    .stream()
                    .map(this::unwrapBase)
                    .collect(Collectors.toList());
                gson.toJson(baseApps, writer);
            }
            System.out.println("All data saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving JSON data: " + e.getMessage());
        }
    }
}
