## SonarQube Test Data Importer Plugin
Simple plugin to import unit test reports and coverate. Supports xunit, gtest and nunit for tests metrics and a json format for coverage.

## Features
1. unit test metrics
2. coverage metrics

## Motivation
I needed to create branch/condition coverage in sonar in a way that merged reports are actually making sence. Currently (6.3) saving coverage measures is done like this
``
newCoverage.conditions(measure.getLine(), measure.getConditions(), measure.getCoveredConditions());
``

With this we are unable to specific what codition/branches have been covered. For example many coverage tools provide this information, like opencover, so when using existing coverage imports or
codition coverage is not counted at all (as in c# plugin) or all they allow you to feed multiple reports and then the behaviour is not deterministic. 
Maybe this situation will change in future, however for now, it seems SonarQube folks give the responsibility to handle this behaviour to users. So when using generic coverate in 6.3 you
must ensure all data is first merged into one single file (if you use more than one it does not work). 

For that reason, this plugin introduces a new coverage format when you can specify branch ids. With this all data is initially parsed and cached and merged together. And then used per module when files have been found.

This method supports opencover really well, and at this moment this is the only way to collect branch/condition coverage for it. You need to use the OpenCover converter application to create the coverage json. But after this all should be cleanly displayed in Sonar.

## Quickstart
1. Setup a SonarQube instance
2. Install the plugin
3. sonar.tests.coverage.reportPath -> for coverage report, example: coverage.json
   sonar.tests.unit.reportPath -> for unit test report, example: test.xml
   sonar.tests.unit.xunit.xsltURL -> for unit test report transformation
   
## Coverage Format

>>
[{
	"file": "C:\\Users\\punker76\\Documents\\GitHub\\MahApps.Metro\\MahApps.Metro\\Accent.cs",
	"lines": [{
		"line": "20",
        "covered" : "true",
		"branches": [{
			"branch": "1",
			"covered": "false"
		},
		{
			"branch": "2",
			"covered": "true"
		}]
	},
	{
		"line": "21",
        "covered" : "true",
		"branches": [{
			"branch": "1",
			"covered": "false"
		},
		{
			"branch": "2",
			"covered": "true"
		}]
	}]
},
{
	"file": "C:\\Users\\punker76\\Documents\\GitHub\\MahApps.Metro\\MahApps.Metro\\Accent2.cs",
	"lines": [{
		"line": "20",
        "covered" : "true",
		"branches": [{
			"branch": "1",
			"covered": "true"
		},
		{
			"branch": "2",
			"covered": "false"
		}]
	},
	{
		"line": "21",
        "covered" : "true",
		"branches": [{
			"branch": "1",
			"covered": "true"
		},
		{
			"branch": "2",
			"covered": "false"
		}]
	}]
},
]

