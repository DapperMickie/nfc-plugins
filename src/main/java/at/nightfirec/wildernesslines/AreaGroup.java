package at.nightfirec.wildernesslines;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class AreaGroup
{
	@Getter
	private String name;

	@Getter
	private AreaOrigin origin;

	@Getter
	private AreaType areaType;

	@Getter
	private Area[] areas;
}
