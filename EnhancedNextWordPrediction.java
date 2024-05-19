package wordpredictor;

import java.util.*;

public class EnhancedNextWordPrediction {

    private Map<String, Map<String, Map<String, Integer>>> trigramCounts = new HashMap<>();

    
    public void train(String text) {
        String[] words = text.split("\\s+");
        for (int i = 0; i < words.length - 2; i++) {
            String firstWord = words[i].toLowerCase();
            String secondWord = words[i + 1].toLowerCase();
            String thirdWord = words[i + 2].toLowerCase();
            
            trigramCounts.putIfAbsent(firstWord, new HashMap<>());
            Map<String, Map<String, Integer>> secondWordMap = trigramCounts.get(firstWord);
            secondWordMap.putIfAbsent(secondWord, new HashMap<>());
            Map<String, Integer> thirdWordMap = secondWordMap.get(secondWord);
            thirdWordMap.put(thirdWord, thirdWordMap.getOrDefault(thirdWord, 0) + 1);
        }
    }

    
    public String predictNextWord(String firstWord, String secondWord) {
        firstWord = firstWord.toLowerCase();
        secondWord = secondWord.toLowerCase();
        if (!trigramCounts.containsKey(firstWord) || !trigramCounts.get(firstWord).containsKey(secondWord)) {
            return null;
        }
        Map<String, Integer> thirdWordCounts = trigramCounts.get(firstWord).get(secondWord);
        return thirdWordCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public static void main(String[] args) {
        EnhancedNextWordPrediction predictor = new EnhancedNextWordPrediction();
        
        // Sample text for training
        String text = "This is a sample text. This text is for testing the next word prediction model. Predict the next word.";
        predictor.train(text);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter text to predict the next word (or type 'exit' to quit):");

        List<String> inputWords = new ArrayList<>();

        while (true) {
            System.out.print("Input text: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            inputWords.addAll(Arrays.asList(input.split("\\s+")));
            while (inputWords.size() >= 2) {
                String firstWord = inputWords.get(inputWords.size() - 2);
                String secondWord = inputWords.get(inputWords.size() - 1);

                String prediction = predictor.predictNextWord(firstWord, secondWord);
                if (prediction != null) {
                    System.out.println("Next word prediction: " + prediction);
                    inputWords.add(prediction);
                } else {
                    System.out.println("No prediction available for the given words.");
                    break;
                }
            }
        }

        scanner.close();
    }
}

