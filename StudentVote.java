import java.util.*; 

class Student {
    // ID for the student, answer submitted by the student
    private String id; 
    private String answer; 

    public Student(String id) {
        this.id = id; // Initialize the student with ID
    }

    public String getId() {
        return id; // Returns ID
    }

    public void submitAns(String answer) {
        this.answer = answer; // Store the answer
    }

    public String getAns() {
        return answer; // Return the answer
    }
}

abstract class Question {
    protected String question; // The text of the question
    protected List<String> candidateAns; // List of possible answers to the question

    public Question(String question, List<String> candidateAns) {
        this.question = question; // Initialize the text
        this.candidateAns = candidateAns; // Initializing candidate answers
    }

    public String getQ() {
        return question; // Return the text
    }

    public List<String> getCanAns() {
        return candidateAns; // Return the candidate answers
    }

    public abstract boolean validateAns(String answer); // Abstract method to validate the submitted answer
}

class MultChoiceQ extends Question {

    public MultChoiceQ(String question, List<String> candidateAns) {
        super(question, candidateAns); // Initialize the question and candidate answers
    }

    @Override
    public boolean validateAns(String answer) {
        String[] answers = answer.split(","); // Split the answer into multiple choices
        for (String ans : answers) {
            if (!candidateAns.contains(ans.trim())) {
                return false; // Return false if any of the choices is not in answers
            }
        }
        return true; // Return true if all choices are valid
    }
}

class SingChoiceQ extends Question {

    public SingChoiceQ(String question, List<String> candidateAns) {
        super(question, candidateAns); // Initialize the question and candidate answers
    }

    @Override
    public boolean validateAns(String answer) {
        return candidateAns.contains(answer); // Return true if the answer is in the candidate answers
    }
}

class VotingService {
    private Question question; // The current question being answered
    private Map<String, String> submissions; // Map of student IDs to their submitted answers

    public VotingService() {
        this.submissions = new HashMap<>(); // Initialize the submissions map
    }

    public void configureQuestion(Question question) {
        this.question = question; // Set the current question
        this.submissions.clear(); // Clear previous submissions
    }

    public void submitAns(Student student) {
        if (question.validateAns(student.getAns())) {
            submissions.put(student.getId(), student.getAns()); // Store the student's answer if it is valid
        }
    }

    public void printResults() {
        Map<String, Integer> resultCount = new HashMap<>(); // Map to count the number of each answer
        for (String answer : submissions.values()) {
            if (question instanceof MultChoiceQ) {
                String[] answers = answer.split(",");
                for (String ans : answers) {
                    ans = ans.trim();
                    resultCount.put(ans, resultCount.getOrDefault(ans, 0) + 1); // Increment count for each choice
                }
            } else {
                resultCount.put(answer, resultCount.getOrDefault(answer, 0) + 1); // Increment count for the single choice
            }
        }

        // Print out the results
        for (Map.Entry<String, Integer> entry : resultCount.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue()); // Print each answer and its count
        }
    }
}

class SimulationDriver {
    public static void main(String[] args) {
        // Create question and candidate answers

        // Possible answers for multiple-choice question
        List<String> mcAnswers = Arrays.asList("A", "B", "C", "D"); 
        Question mcQuestion = new MultChoiceQ("What is your favorite wild animal?", mcAnswers);

        // Possible answers for single-choice question
        List<String> scAnswers = Arrays.asList("1. Right", "2. Wrong"); 
        Question scQuestion = new SingChoiceQ("Does the chicken or egg come first?", scAnswers);

        // Create a new VotingService
        VotingService votingService = new VotingService();

        // Configure the iVote Service with a multiple-choice question
        votingService.configureQuestion(mcQuestion); // Configure the VotingService with a multiple-choice question

        // Simulate student submissions for multiple-choice question
        Random rand = new Random(); // Create a random number generator
        for (int i = 0; i < 100; i++) {
            Student student = new Student("Student" + i); // Create a new student with a unique ID
            String answer = mcAnswers.get(rand.nextInt(mcAnswers.size())); // Select a random answer
            if (rand.nextBoolean()) {
                answer += ", " + mcAnswers.get(rand.nextInt(mcAnswers.size())); // Occasionally add a second random answer
            }
            student.submitAns(answer); // Submit the answer for the student
            votingService.submitAns(student); // Submit the student's answer to the VotingService
        }

        // Print results for multiple-choice question
        System.out.println("Results for multiple-choice question");
        System.out.println("Here are the final answers:");
        votingService.printResults(); // Print the results of the voting

        // Configure the iVote Service with a single-choice question
        votingService.configureQuestion(scQuestion); // Reconfigure the VotingService with a single-choice question

        // Simulate student submissions for single-choice question
        for (int i = 0; i < 100; i++) {
            Student student = new Student("Student" + i); // Create a new student with a unique ID
            String answer = scAnswers.get(rand.nextInt(scAnswers.size())); // Select a random answer
            student.submitAns(answer); // Submit the answer for the student
            votingService.submitAns(student); // Submit the student's answer to the VotingService
        }

        // Print results for single-choice question
        System.out.println("Results for single-choice question");
        System.out.println("Here are the final answers:");
        votingService.printResults(); // Print the results of the voting
    }
}
