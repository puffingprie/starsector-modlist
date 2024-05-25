package data.scripts.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.plugins.LevelupPlugin;

public class LevelupPluginImpl implements LevelupPlugin {

	/**
	 * Only used if max level is increased beyond 15 via settings.json
	 */
	public static float EXPONENT_BEYOND_MAX_SPECIFIED_LEVEL = 1.1f;
	
	/**
	 * Max level XP times this is how much XP it takes to gain storyPointsPerLevel story points once at max level.
	 */
	public static float XP_REQUIRED_FOR_STORY_POINT_GAIN_AT_MAX_LEVEL_MULT = 1f;
	public static int LEVEL_FOR_BASE_XP_FOR_MAXED_STORY_POINT_GAIN = 38;
	//65680000
	public static long [] XP_PER_LEVEL = new long [] {
		0,		// level 1
		40000,									//40k
		60000,									//20k
		80000,									//20k
		100000,  // level 5, ramp up after		//20k
		160000,									//60k
		220000,									//60k
		280000,									//60k
		340000,									//60k
		400000, // level 10, ramp up after		//60k
		480000,									//80k
		560000,									//80k
		640000,									//80k
		720000,									//80k
		800000, // level 15						//80k
		900000,									//100k
		1000000,								//100k
		1100000,								//100k
		1200000,								//100k
		1300000, // level 20					//100k
		1420000,								//120k
		1540000,								//120k
		1660000,								//120k
		1780000,								//120k
		1900000, //level 25						//120k
		2040000,								//140k
		2180000,								//140k
		2320000,								//140k
		2460000,								//140k
		2600000, //level 30						//140k
		2760000,								//160k
		2920000,								//160k
		3080000,								//160k
		3240000,								//160k
		3400000, //level 35						//160k
		3600000,								//200k
		3800000,								//200k
		4000000,								//200k
		4200000,								//200k
		4400000, //level 40						//200k
	};
	
	public static long [] TOTAL_XP_PER_LEVEL = new long [XP_PER_LEVEL.length];
	
	static {
		long total = 0;
		for (int i = 0; i < XP_PER_LEVEL.length; i++) {
			total += XP_PER_LEVEL[i];
			TOTAL_XP_PER_LEVEL[i] = total;
		}
	}
	                                               
	                                              
	
	
	
	public int getPointsAtLevel(int level) {
		return (int) Global.getSettings().getFloat("skillPointsPerLevel");
	}

	public int getMaxLevel() {
		return (int) Global.getSettings().getFloat("playerMaxLevel");
	}
	
	public int getStoryPointsPerLevel() {
		return (int) Global.getSettings().getFloat("storyPointsPerLevel");
	}
	
	public int getBonusXPUseMultAtMaxLevel() {
		return (int) Global.getSettings().getFloat("bonusXPUseMultAtMaxLevel");
	}
	
//	public long getXPForNextLevel(int level) {
//		if (level < XP_PER_LEVEL.length) {
//			return XP_PER_LEVEL[level];
//		}
//		
//		return (long) (XP_PER_LEVEL[LEVEL_FOR_BASE_XP_FOR_MAXED_STORY_POINT_GAIN - 1] * XP_REQUIRED_FOR_STORY_POINT_GAIN_AT_MAX_LEVEL_MULT);
//	}

	
	public long getXPForLevel(int level) {
		if (level <= 1) return 0;
		
		if (level - 1 < TOTAL_XP_PER_LEVEL.length) {
			return TOTAL_XP_PER_LEVEL[level - 1];
		}
		
		int max = getMaxLevel();
		int maxSpecified = TOTAL_XP_PER_LEVEL.length;
		long curr = TOTAL_XP_PER_LEVEL[maxSpecified - 1];
		long last = XP_PER_LEVEL[maxSpecified - 1];
		for (int i = maxSpecified; i < level && i < max; i++) {
			last *= EXPONENT_BEYOND_MAX_SPECIFIED_LEVEL;
			curr += last;
		}
		
		if (level >= max + 1) {
			//last *= XP_REQUIRED_FOR_STORY_POINT_GAIN_AT_MAX_LEVEL_MULT;
			last = (long) (XP_PER_LEVEL[LEVEL_FOR_BASE_XP_FOR_MAXED_STORY_POINT_GAIN - 1] * 
						   XP_REQUIRED_FOR_STORY_POINT_GAIN_AT_MAX_LEVEL_MULT);
			curr += last;
		}
		
		return curr;
	}

	
	public static void main(String[] args) {
		LevelupPluginImpl plugin = new LevelupPluginImpl();
		
//		System.out.println(plugin.getXPForLevel(16) - plugin.getXPForLevel(15));
		
		for (int i = 1; i < 16; i++) {
			//System.out.println(i + ": " + (plugin.getXPForLevel(i) - plugin.getXPForLevel(i - 1)));
			System.out.println(i + ": " + (plugin.getXPForLevel(i)));
		}
	}


}
