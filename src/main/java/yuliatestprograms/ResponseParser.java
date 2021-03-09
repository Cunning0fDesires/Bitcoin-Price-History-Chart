package yuliatestprograms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jfree.data.time.Hour;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

   /* This class takes the API's response, checks the lengths of each entry,
  filters time and price values and edits the format of the dates */

public class ResponseParser {
    String json;
    public ResponseParser(String json) throws JsonProcessingException {
        this.json = json;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode map = mapper.readTree(json); //parsing the response data, using JsonNode to avoid hash collisions
        populateLists(map); //get the lists populated with the sorted time and price values
    }

    private void populateLists(JsonNode map) {
  //looking for a value by its key, adding it to its corresponding sorted list
        IntStream.range(0, map.size()).forEach(i -> closePrices.add(map.get(i).findValue("close").asDouble()));
        IntStream.range(0, map.size()).forEach(i -> dates
                .add(new Hour(
                        Date.from(Instant.parse(map.get(i)
                                .findValue("time")
                                .asText()))))); //get time values by its key, parsing it as an Hour instance because this library does not accept Java's native date formats
        IntStream.range(0, map.size()).forEach(i -> datesAsInts.add(i++));
        boolean lengthCheck = Stream.of( //check if the length of each entry is the same
                closePrices.size(), //get the length of each entry
                dates.size(),
                datesAsInts.size()
                ).distinct() //remove the duplicate elements from the stream
                .count() == 1; //return the number of unique elements. There must be one left if the length of each is the same
        if (!lengthCheck)
            throw new RuntimeException( //if the length varies, throw an exception
                    "The lengths of each data value do not match");
    }

   //lists for the sorted values for the chart
   //used LinkedList to preserve the order
    private List<Double> closePrices = new LinkedList<>();
    private List<Hour> dates = new LinkedList<>();
    private List<Integer> datesAsInts = new LinkedList<>();

    public List<Integer> getDatesAsInts() { return datesAsInts; } //getters
    public List<Hour> getDates() { return dates; }
    public List<Double> getClosePrices() { return closePrices; }
    //the number of elements in each entry, so it can be any field
}
