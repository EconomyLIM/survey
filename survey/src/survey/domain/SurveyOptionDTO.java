package survey.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SurveyOptionDTO {
	
	private int surveyId;
	private int contentId;
	private String optionContent;
	private int optionCnt;
}
