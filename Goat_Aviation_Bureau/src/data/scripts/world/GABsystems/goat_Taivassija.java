package data.scripts.world.GABsystems;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI.SurveyLevel;
import com.fs.starfarer.api.impl.campaign.ids.Terrain;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.ShipRecoverySpecial;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.ShipRecoverySpecial.ShipCondition;
import com.fs.starfarer.api.impl.campaign.terrain.MagneticFieldTerrainPlugin;
import org.magiclib.util.MagicCampaign;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import static com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.ShipRecoverySpecial.ShipCondition.BATTERED;

public class goat_Taivassija implements SectorGeneratorPlugin {

	public ShipCondition condition = ShipCondition.AVERAGE;
	public ShipCondition condition1 = ShipCondition.PRISTINE;
	public ShipCondition condition2 = ShipCondition.BATTERED;

	@Override
	public void generate(SectorAPI sector) {

		StarSystemAPI system = sector.createStarSystem("Taivassija");
		system.getLocation().set(-11000, -19400);

		LocationAPI hyper = Global.getSector().getHyperspace();
		EconomyAPI globalEconomy = Global.getSector().getEconomy();

		// 下面这行的引号内内容填写你星系背景图片的路径 从graphics文件夹开始导航
		system.setBackgroundTextureFilename("graphics/goat/backgrounds/goat_background1.png");

		// 创建恒星并为当前恒星系生成超空间跳跃点
		PlanetAPI Star = system.initStar("GAB001", "star_blue_giant", 850f, 150f);
		system.setLightColor(new Color(180, 185, 255));

		system.addAsteroidBelt(Star, 100, 4150, 128, 200, 300, Terrain.ASTEROID_BELT, null);
		system.addAsteroidBelt(Star, 100, 4250, 188, 200, 300, Terrain.ASTEROID_BELT, null);
		system.addAsteroidBelt(Star, 100, 4375, 256, 200, 300, Terrain.ASTEROID_BELT, null);

		system.addRingBand(Star, "misc", "rings_asteroids0", 256f, 0, Color.white, 256f, 4000, 80f);
		system.addRingBand(Star, "misc", "rings_asteroids0", 256f, 1, Color.white, 256f, 4100, 120f);
		system.addRingBand(Star, "misc", "rings_asteroids0", 256f, 2, Color.white, 256f, 4200, 160f);

		system.addRingBand(Star, "misc", "rings_dust0", 256f, 3, Color.white, 256f, 4300, 140f);
		system.addRingBand(Star, "misc", "rings_dust0", 256f, 2, Color.white, 256f, 4400, 180f);
		system.addRingBand(Star, "misc", "rings_dust0", 256f, 1, Color.white, 256f, 4500, 220f);

		system.addRingBand(Star, "misc", "rings_ice0", 256f, 0, Color.white, 256f, 4500, 100f);
		system.addRingBand(Star, "misc", "rings_ice0", 256f, 2, Color.white, 256f, 4600, 140f);
		system.addRingBand(Star, "misc", "rings_ice0", 256f, 1, Color.white, 256f, 4700, 160f);

		PlanetAPI Star2 = system.addPlanet("GAB002", Star, "Xiuhcoatl", "star_white", 45, 120, 13000, 200);
		system.setSecondary(Star2);
		system.addCorona(Star2, 150, 3f, 0.05f, 1f); // it's a very docile star.

		PlanetAPI GAB_noman1 = system.addPlanet("Huitzilopochtli", Star, "Huitzilopochtli", "gas_giant", 0, 280, 1550, 40);
		GAB_noman1.getSpec().setPlanetColor(new Color(50, 100, 255, 255));
		GAB_noman1.getSpec().setAtmosphereColor(new Color(120, 130, 100, 150));
		GAB_noman1.getSpec().setCloudColor(new Color(195, 230, 255, 200));
		GAB_noman1.getSpec().setIconColor(new Color(220, 130, 200, 255));
		GAB_noman1.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "aurorae"));
		GAB_noman1.getSpec().setGlowColor(new Color(235, 38, 110, 145));
		GAB_noman1.getSpec().setUseReverseLightForGlow(true);
		GAB_noman1.getSpec().setAtmosphereThickness(0.5f);
		GAB_noman1.applySpecChanges();
		GAB_noman1.setCustomDescriptionId("planet_Huitzilopochtli");

		SectorEntityToken magec1_field = system.addTerrain(Terrain.MAGNETIC_FIELD, new MagneticFieldTerrainPlugin.MagneticFieldParams(200f, // terrain effect band width
				380, // terrain effect middle radius
				GAB_noman1, // entity that it's around
				280f, // visual band start
				480f, // visual band end
				new Color(50, 30, 100, 30), // base color
				1f, // probability to spawn aurora sequence, checked once/day when no aurora in progress
				new Color(50, 20, 110, 130), new Color(150, 30, 120, 150), new Color(200, 50, 130, 190), new Color(250, 70, 150, 240), new Color(200, 80, 130, 255), new Color(75, 0, 160), new Color(127, 0, 255)));
		magec1_field.setCircularOrbit(GAB_noman1, 0, 0, 100);

		PlanetAPI GAB1 = system.addPlanet("GAB1", Star, "3rd Babel", "lava_minor", 60, 150, 3200, 210);//添加星球参数分别为ID,轴心,展示名,星球类型,星球倾斜角度,轨道半径,轨道天数

		PlanetAPI GAB3 = system.addPlanet("GAB3", Star, "Theia", "cryovolcanic", 60, 110, 6600, 210); //添加星球参数分别为ID,轴心,展示名,星球类型,星球倾斜角度,轨道半径,轨道天数
		PlanetAPI GAB4 = system.addPlanet("GAB4", Star, "Fomalhaut", "gas_giant", 20, 320, 7600, 610);
		GAB4.getSpec().setPlanetColor(new Color(50, 200, 255, 255));
		GAB4.getSpec().setAtmosphereColor(new Color(120, 250, 100, 160));
		GAB4.getSpec().setCloudColor(new Color(195, 230, 255, 150));
		GAB4.getSpec().setIconColor(new Color(220, 130, 200, 185));
		GAB4.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "aurorae"));
		GAB4.getSpec().setGlowColor(new Color(135, 238, 110, 145));
		GAB4.getSpec().setUseReverseLightForGlow(true);
		GAB4.getSpec().setAtmosphereThickness(0.5f);
		GAB4.applySpecChanges();
		GAB4.setCustomDescriptionId("planet_Fomalhaut");

		PlanetAPI GAB5 = system.addPlanet("Star2", Star, "Coatepec", "cryovolcanic", 20, 100, 9500, 610);

		SectorEntityToken GAB12 = system.addCustomEntity("GAB11_yuanjiang", "Babel Station", "station_midline3", "Goat_Aviation_Bureau");
		GAB12.setCustomDescriptionId("station_GAB11_yuanjiang");
		GAB12.setInteractionImage("illustrations", "farways_illustrations");
		GAB12.setCircularOrbitPointingDown(GAB1, 60, 400, 120);
		MarketAPI GAB12M = MagicCampaign.addSimpleMarket(GAB12, "GAB11_yuanjiang_market", GAB12.getName(), 7, "Goat_Aviation_Bureau", true, false, new ArrayList<>(Arrays.asList("population_7", "habitable")), new ArrayList<>(Arrays.asList("population", "megaport", "orbitalworks", "starfortress", "heavybatteries", "highcommand", "waystation")), true, false, true, true, true, false);
		GAB12M.getIndustry("orbitalworks").setSpecialItem(new SpecialItemData("pristine_nanoforge", ""));
		globalEconomy.addMarket(GAB12M, true);

		SectorEntityToken GAB13 = system.addCustomEntity("GAB11_suihen", "Mindscar Station", "station_midline2", "Goat_Aviation_Bureau");
		GAB13.setCustomDescriptionId("station_GAB11_suihen");
		GAB13.setInteractionImage("illustrations", "suihen_illustrations");
		GAB13.setCircularOrbitPointingDown(Star, 60, 6200, 120);
		MarketAPI GAB13M = MagicCampaign.addSimpleMarket(GAB13, "GAB11_suihen_market", GAB13.getName(), 4, "Goat_Aviation_Bureau", true, false, new ArrayList<>(Arrays.asList("population_4")), new ArrayList<>(Arrays.asList("population", "spaceport", "heavyindustry", "battlestation_mid", "heavybatteries", "patrolhq")), true, false, true, true, false, false);
		globalEconomy.addMarket(GAB13M, true);

		SectorEntityToken Heaven_Ladder_buoy = system.addCustomEntity("Heaven_Ladder_buoy", // unique id
				"Babel Buoy", // name - if null, defaultName from custom_entities.json will be used //haha bababooey
				"nav_buoy", // type of object, defined in custom_entities.json
				"Goat_Aviation_Bureau"); // factio
		Heaven_Ladder_buoy.setCircularOrbitPointingDown(Star, 45 + 60, 12000, 700);

		SectorEntityToken permasa_relay = system.addCustomEntity("permasa_relay", // unique id
				"Permasa Relay", // name - if null, defaultName from custom_entities.json will be used
				"comm_relay", // type of object, defined in custom_entities.json
				"Goat_Aviation_Bureau"); // faction
		permasa_relay.setCircularOrbitPointingDown(Star, 5 - 60, 6600, 200);

		PlanetAPI GAB51 = system.addPlanet("GAB5", Star, "Xiuhcoatl-1", "lava", 40, 130, 11300, 200);
		PlanetAPI GAB52 = system.addPlanet("GAB5", GAB51, "Xiuhcoatl-1-1", "lava", 40, 50, 500, 110);
		PlanetAPI GAB53 = system.addPlanet("GAB5", Star, "Xiuhcoatl-2", "lava", 40, 80, 11500, 220);


		if (Math.random() < 0.7) {
			MagicCampaign.createDerelict("goat_CROWN_Assault", condition,false,0,true,GAB51,4, 300, 120
			);
			if (Math.random() < 0.4) {
				MagicCampaign.createDerelict("goat_CROWN_Assault", condition1,false,0,true,Star,4, 11700, 110
				);
			}
		}
		if (Math.random() < 0.6) {
			MagicCampaign.createDerelict("goat_TOWER_Assault", condition1, false, 0, true, GAB52, 4, 500, 120
			);
			if (Math.random() < 0.1) {
				MagicCampaign.createDerelict("goat_TOWER_Assault", condition, false, 0, true, GAB51, 4, 100, 120
				);
			}
		}
		if (Math.random() < 0.7) {
			MagicCampaign.createDerelict("goat_THRONE_Assault", condition2, false, 0, true, GAB53, 4, 200, 420
			);
		}
		if (Math.random() < 0.6) {
			MagicCampaign.createDerelict("goat_DIRK_Assault", condition2, false, 0, true, GAB52, 4, 200, 120
			);
			MagicCampaign.createDerelict("goat_DIRK_Assault", condition2, false, 0, true, GAB53, 4, 400, 120
			);
			if (Math.random() < 0.5) {
				MagicCampaign.createDerelict("goat_DIRK_Assault", condition, false, 0, true, GAB53, 4, 700, 80
				);
			}
		}
		if (Math.random() < 0.9) {
			MagicCampaign.createDerelict("goat_FLY_Assault", condition2, false, 0, true, GAB51, 4, 350, 220
			);
			if (Math.random() < 0.3) {
				MagicCampaign.createDerelict("goat_FLY_Assault", condition1, false, 0, true, GAB53, 4, 300, 120
				);
			}
		}
		if (Math.random() < 0.9) {
			MagicCampaign.createDerelict("goat_AWL_Assault", condition2, false, 0, true, GAB51, 4, 350, 220
			);
			if (Math.random() < 0.3) {
				MagicCampaign.createDerelict("goat_AWL_Assault", condition1, false, 0, true, GAB53, 4, 300, 120
				);
			}
		}

		if (Math.random() < 0.2) {
			MagicCampaign.createDerelict("goat_GRANDEUR_oldday", condition, false, 0, true, Star, 4, 11300, 120
			);
		}

		JumpPointAPI jumpPoint1 = Global.getFactory().createJumpPoint("GAB_JumpPoint1", "Heaven's Gate");
		OrbitAPI orbit = Global.getFactory().createCircularOrbit(GAB1, 220, 650, 365); //创建一个圆形轨道 参数列表分别为 轴心,角度,轨道半径,轨道周期
		jumpPoint1.setOrbit(orbit); //为跳跃点设置轨道
		jumpPoint1.setRelatedPlanet(GAB1); //为跳跃点设置关联行星 参数填入行星变量名
		jumpPoint1.setStandardWormholeToHyperspaceVisual();
		system.addEntity(jumpPoint1); //为星系添加自定义实体 也就是刚刚创建好的跳跃点
		//添加环带 参数列表分别为 轴心,类型,环的ID,纹理宽度,纹理序号,颜色,这一个参数不太清楚什么意思,环带半径,周期天数

		system.addRingBand(Star, "misc", "rings_ice0", 2000f, 3, Color.blue, 1000f, 5400, 300f);
		system.addRingBand(Star, "misc", "rings_ice0", 2000f, 3, Color.blue, 1000f, 5600, 300f);

		//system.addRingBand(Star, "misc", "rings_dust0", 11400f, 0, Color.white, 256f, 3200, 80f);

		system.addRingBand(Star, "misc", "rings_dust0", 256f, 1, Color.white, 256f, 11500, 220f);

		//添加小行星带 参数列表为 轴心,小行星数量,轨道半径,宽度,最小周期天数,最大周期天数
		//system.addAsteroidBelt(Star, 300, 1200, 300, 200, 300);
		system.addAsteroidBelt(Star, 250, 6000, 500, 200, 300);
		system.addAsteroidBelt(Star, 150, 5200, 200, 200, 300);
		system.addAsteroidBelt(Star, 150, 5700, 300, 200, 300);
		system.addAsteroidBelt(Star, 1000, 11400, 500, 200, 300);
		system.addAsteroidBelt(GAB51, 50, 300, 30, 200, 300);
		system.addAsteroidBelt(GAB51, 50, 100, 90, 200, 300);
		system.addAsteroidBelt(GAB53, 50, 300, 30, 200, 300);

		system.autogenerateHyperspaceJumpPoints(true, true); //设置自动生成跳跃点
		for (PlanetAPI planet : system.getPlanets()) {
			if (planet.getMarket() == null) continue;

			planet.getMarket().setSurveyLevel(SurveyLevel.FULL);
		}

		MagicCampaign.hyperspaceCleanup(system); //完成生成
	}
}