qSolarSim
---------

Authors: Mike Rooke and Eyrak Paen


About
-----

This program was used to simulate and display data about the Queen's University Solar Vehicle "Ultraviolet" when the car was used in the North American Solar Challenge in 2005. The program was used in conjunction with a telemetry computer that gathered data remotely from the car and logged the data in a SQL database. As such, the full use of the program is impossible without fake data, but simulations are still possible.

Purpose
-------

The software in its current form is mainly used as a codebase on which other teams or individuals can learn from or extend upon. Most of the functionality is obtained when used with an external database that is updated in real time. Much of the programs structure was designed with this in mind. The interesting sections of code are mainly found in the org.qsvt.solarstrat.solarsim package.


Example Use
-----------

The program's entry point is in the org.qsvt.solarstrat.ui.SolarstratMainWindow class (i.e. main() is located there). If you have checked out a copy of the program from the CVS repository, simply tell java to use this as the entry point. If you have downloaded the windows executable, just run the program.

The quickest way to appreciate the simulation capability is to select Simulation->New Simulation... and click on "Default Test". A new leaf in the Simulations tree should appear. Highlight it, then select Simulation->Start Simulation. Output of the simulation should appear momentarily. 

