9
(Lift hoist1 crate10 crate1 depot1)
(Unload hoist1 crate14 truck0 distributor0)
(Unload hoist1 crate13 truck0 depot1)
(Drop hoist5 crate12 pallet5 distributor2)
(Drop hoist1 crate14 crate10 depot1)
(Lift hoist1 crate1 pallet5 depot1)
(Drop hoist1 crate13 pallet1 depot1)
(Drop hoist1 crate10 crate13 distributor0)
(Drop hoist1 crate1 crate12 distributor0)
8
(:init
	(on crate5 pallet0)
	(on crate4 crate0)
	(at hoist3 distributor0)
	(available hoist3)
	(at crate6 distributor1)
	(at crate3 distributor0)
	(at crate13 distributor1)
	(at crate4 depot2)
	(at pallet1 depot1)
	(at truck1 distributor2)
	(at crate3 depot2)
	(on crate9 pallet2)
	(available hoist4)
	(on crate4 crate3)
	(at hoist0 depot0)
	(on crate0 pallet1)
	(at crate1 depot0)
	(on crate3 pallet1)
	(at crate0 distributor0)
	(at truck1 depot0)
	(available hoist2)
	(on crate13 crate12)
	(at hoist1 depot1)
	(on crate3 crate2)
	(at crate9 distributor1)
	(on crate0 pallet0)
	(at crate14 distributor0)
	(on crate1 pallet1)
	(available hoist1)
	(at crate3 depot0)
	(clear pallet3)
	(at crate6 depot0)
	(on crate14 crate9)
	(on crate3 crate1)
	(at hoist2 depot2)
	(clear crate3)
	(at crate1 distributor1)
	(on crate12 crate11)
	(on crate1 pallet2)
	(on crate10 crate1)
	(available hoist0)
	(in crate4 truck1)
	(at pallet4 distributor0)
	(in crate11 truck0)
	(at crate11 distributor0)
	(at crate10 distributor1)
	(at crate9 depot2)
	(on crate1 pallet5)
	(at pallet5 depot0)
	(on crate12 crate9)
	(at pallet0 depot0)
	(at pallet5 distributor2)
	(on crate7 crate4)
	(clear crate5)
	(clear pallet5)
	(at pallet1 distributor0)
	(at crate0 depot0)
	(at pallet3 distributor1)
	(at truck0 depot1)
	(at crate0 depot1)
	(at truck0 depot2)
	(at pallet5 distributor1)
	(at pallet2 distributor1)
	(on crate6 pallet1)
	(at hoist5 distributor2)
	(at crate4 depot0)
	(on crate5 crate4)
	(clear crate6)
	(on crate14 crate13)
	(clear crate10)
	(on crate9 crate7)
	(at pallet4 distributor1)
	(at crate1 depot1)
	(at truck0 distributor0)
	(on crate2 pallet2)
	(on crate2 crate1)
	(at crate3 depot1)
	(at crate5 distributor1)
	(at crate10 depot1)
	(on crate9 crate5)
	(clear pallet1)
	(on crate5 pallet3)
	(at crate2 depot2)
	(in crate7 truck1)
	(in crate14 truck0)
	(on crate6 crate1)
	(at hoist1 distributor0)
	(on crate8 pallet5)
	(at crate2 distributor1)
	(at crate7 distributor0)
	(at crate2 depot0)
	(at pallet3 distributor0)
	(at crate12 distributor0)
	(on crate6 crate2)
	(at crate8 distributor0)
	(at pallet2 depot2)
	(on crate13 crate4)
	(at crate11 depot0)
	(on crate0 pallet2)
	(on crate6 pallet5)
	(on crate11 crate6)
	(at crate4 distributor1)
	(in crate8 truck1)
	(at crate6 distributor0)
	(at crate3 distributor1)
	(on crate5 crate2)
	(at crate5 depot2)
	(at crate9 distributor0)
	(on crate0 pallet4)
	(at crate0 distributor1)
	(on crate2 crate0)
	(at crate8 depot0)
	(in crate13 truck0)
	(on crate1 pallet0)
	(lifting hoist5 crate12)
	(at crate13 distributor0)
	(at crate5 depot0)
	(on crate3 crate9)
	(at hoist4 distributor1)
	(at hoist2 distributor1)
)
(:add
	(lifting hoist1 crate13)
	(on crate14 crate10)
	(available hoist1)
	(at crate14 depot1)
	(clear crate14)
	(lifting hoist1 crate10)
	(clear crate1)
	(available hoist1)
	(at crate13 depot1)
	(on crate13 pallet1)
	(clear crate13)
	(lifting hoist1 crate1)
	(clear pallet5)
	(available hoist5)
	(at crate12 distributor2)
	(clear crate12)
	(on crate12 pallet5)
	(available hoist1)
	(clear crate1)
	(on crate1 crate12)
	(at crate1 distributor0)
	(lifting hoist1 crate14)
	(available hoist1)
	(at crate10 distributor0)
	(clear crate10)
	(on crate10 crate13)
)
(:delete
	(available hoist1)
	(in crate13 truck0)
	(clear crate10)
	(lifting hoist1 crate14)
	(available hoist1)
	(at crate10 depot1)
	(on crate10 crate1)
	(clear crate10)
	(lifting hoist1 crate13)
	(clear pallet1)
	(available hoist1)
	(at crate1 depot1)
	(clear crate1)
	(on crate1 pallet5)
	(lifting hoist5 crate12)
	(clear pallet5)
	(lifting hoist1 crate1)
	(clear crate12)
	(in crate14 truck0)
	(available hoist1)
	(lifting hoist1 crate10)
	(clear crate13)
)
(:goal
	(on crate5 pallet0)
	(on crate14 crate10)
	(on crate13 pallet1)
	(on crate0 pallet4)
	(on crate1 crate12)
	(on crate2 crate0)
	(on crate3 crate9)
	(on crate6 crate2)
	(on crate9 pallet2)
	(on crate10 crate13)
	(on crate12 pallet5)
)
(:current
	(on crate5 pallet0)
	(on crate9 pallet2)
	(on crate6 crate2)
	(on crate0 pallet4)
	(on crate2 crate0)
	(on crate3 crate9)
)
