package survey.domain;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SurveyDTO {
	
	private int surveyId;
	private String userId;
	private Date startDate;
	private Date endDate;
	private String title;
	private String state;
	private int surveyAllcnt;
	private Date regdate;
	

}
