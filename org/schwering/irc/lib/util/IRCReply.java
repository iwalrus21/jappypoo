/*      */ package org.schwering.irc.lib.util;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public enum IRCReply
/*      */ {
/*   25 */   RPL_WELCOME(1),
/*      */ 
/*      */ 
/*      */   
/*   29 */   RPL_YOURHOST(2),
/*      */ 
/*      */ 
/*      */   
/*   33 */   RPL_CREATED(3),
/*      */ 
/*      */ 
/*      */   
/*   37 */   RPL_MYINFO(4),
/*      */ 
/*      */ 
/*      */   
/*   41 */   RPL_ISUPPORT(5),
/*      */ 
/*      */ 
/*      */   
/*   45 */   RPL_NONE(300),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   58 */   RPL_USERHOST(302),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   64 */   RPL_ISON(303),
/*      */ 
/*      */ 
/*      */   
/*   68 */   RPL_AWAY(301),
/*      */ 
/*      */ 
/*      */   
/*   72 */   RPL_UNAWAY(305),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   82 */   RPL_NOWAWAY(306),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  101 */   RPL_WHOISUSER(311),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  119 */   RPL_WHOISSERVER(312),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  137 */   RPL_WHOISOPERATOR(313),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  155 */   RPL_WHOISIDLE(317),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  173 */   RPL_ENDOFWHOIS(318),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  191 */   RPL_WHOISCHANNELS(319),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  197 */   RPL_WHOISAUTHNAME(330),
/*      */ 
/*      */ 
/*      */   
/*  201 */   RPL_WHOWASUSER(314),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  211 */   RPL_ENDOFWHOWAS(369),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  220 */   RPL_LISTSTART(321),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  229 */   RPL_LIST(322),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  238 */   RPL_LISTEND(323),
/*      */ 
/*      */ 
/*      */   
/*  242 */   RPL_CHANNELMODEIS(324),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  250 */   RPL_AUTHNAME(333),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  258 */   RPL_NOTOPIC(331),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  266 */   RPL_TOPIC(332),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  272 */   RPL_TOPICINFO(333),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  279 */   RPL_INVITING(341),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  285 */   RPL_SUMMONING(342),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  297 */   RPL_VERSION(351),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  309 */   RPL_WHOREPLY(352),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  320 */   RPL_ENDOFWHO(315),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  336 */   RPL_NAMREPLY(353),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  349 */   RPL_ENDOFNAMES(366),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  357 */   RPL_LINKS(364),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  364 */   RPL_ENDOFLINKS(365),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  377 */   RPL_BANLIST(367),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  387 */   RPL_ENDOFBANLIST(368),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  395 */   RPL_INFO(371),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  403 */   RPL_ENDOFINFO(374),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  413 */   RPL_MOTDSTART(375),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  423 */   RPL_MOTD(372),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  433 */   RPL_ENDOFMOTD(376),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  440 */   RPL_YOUREOPER(381),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  447 */   RPL_REHASHING(382),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  456 */   RPL_TIME(391),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  466 */   RPL_USERSSTART(392),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  476 */   RPL_USERS(393),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  486 */   RPL_ENDOFUSERS(394),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  496 */   RPL_NOUSERS(395),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  520 */   RPL_TRACELINK(200),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  543 */   RPL_TRACECONNECTING(201),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  566 */   RPL_TRACEHANDSHAKE(202),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  589 */   RPL_TRACEUNKNOWN(203),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  612 */   RPL_TRACEOPERATOR(204),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  635 */   RPL_TRACEUSER(205),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  659 */   RPL_TRACESERVER(206),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  682 */   RPL_TRACENEWTYPE(208),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  705 */   RPL_TRACELOG(261),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  711 */   RPL_STATSLINKINFO(211),
/*      */ 
/*      */ 
/*      */   
/*  715 */   RPL_STATSCOMMANDS(212),
/*      */ 
/*      */ 
/*      */   
/*  719 */   RPL_STATSCLINE(213),
/*      */ 
/*      */ 
/*      */   
/*  723 */   RPL_STATSNLINE(214),
/*      */ 
/*      */ 
/*      */   
/*  727 */   RPL_STATSILINE(215),
/*      */ 
/*      */ 
/*      */   
/*  731 */   RPL_STATSKLINE(216),
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  736 */   RPL_STATSYLINE(218),
/*      */ 
/*      */ 
/*      */   
/*  740 */   RPL_ENDOFSTATS(219),
/*      */ 
/*      */ 
/*      */   
/*  744 */   RPL_STATSLLINE(241),
/*      */ 
/*      */ 
/*      */   
/*  748 */   RPL_STATSUPTIME(242),
/*      */ 
/*      */ 
/*      */   
/*  752 */   RPL_STATSOLINE(243),
/*      */ 
/*      */ 
/*      */   
/*  756 */   RPL_STATSHLINE(244),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  762 */   RPL_UMODEIS(221),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  775 */   RPL_LUSERCLIENT(251),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  787 */   RPL_LUSEROP(252),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  799 */   RPL_LUSERUNKNOWN(253),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  811 */   RPL_LUSERCHANNELS(254),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  824 */   RPL_LUSERME(255),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  838 */   RPL_ADMINME(256),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  852 */   RPL_ADMINLOC1(257),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  866 */   RPL_ADMINLOC2(258),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  880 */   RPL_ADMINEMAIL(259),
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  885 */   RPL_TRACECLASS(209),
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  890 */   RPL_STATSQLINE(217),
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  895 */   RPL_SERVICEINFO(231),
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  900 */   RPL_ENDOFSERVICES(232),
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  905 */   RPL_SERVICE(233),
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  910 */   RPL_SERVLIST(234),
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  915 */   RPL_SERVLISTEND(235),
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  920 */   RPL_WHOISCHANOP(316),
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  925 */   RPL_KILLDONE(361),
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  930 */   RPL_CLOSING(362),
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  935 */   RPL_CLOSEEND(363),
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  940 */   RPL_INFOSTART(373),
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  945 */   RPL_MYPORTIS(384),
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  950 */   ERR_NOSUCHNICK(401),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  956 */   ERR_NOSUCHSERVER(402),
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  961 */   ERR_NOSUCHCHANNEL(403),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  969 */   ERR_CANNOTSENDTOCHAN(404),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  977 */   ERR_TOOMANYCHANNELS(405),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  983 */   ERR_WASNOSUCHNICK(406),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  990 */   ERR_TOOMANYTARGETS(407),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  997 */   ERR_NOORIGIN(409),
/*      */ 
/*      */ 
/*      */   
/* 1001 */   ERR_NORECIPIENT(411),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1010 */   ERR_NOTEXTTOSEND(412),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1019 */   ERR_NOTOPLEVEL(413),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1028 */   ERR_WILDTOPLEVEL(414),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1034 */   ERR_UNKNOWNCOMMAND(421),
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1039 */   ERR_NOMOTD(422),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1046 */   ERR_NOADMININFO(423),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1052 */   ERR_FILEERROR(424),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1058 */   ERR_NONICKNAMEGIVEN(431),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1065 */   ERR_ERRONEUSNICKNAME(432),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1072 */   ERR_NICKNAMEINUSE(433),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1079 */   ERR_NICKCOLLISION(436),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1085 */   ERR_USERNOTINCHANNEL(441),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1092 */   ERR_NOTONCHANNEL(442),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1098 */   ERR_USERONCHANNEL(443),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1105 */   ERR_NOLOGIN(444),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1111 */   ERR_SUMMONDISABLED(445),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1117 */   ERR_USERSDISABLED(446),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1124 */   ERR_NOTREGISTERED(451),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1131 */   ERR_NEEDMOREPARAMS(461),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1138 */   ERR_ALREADYREGISTRED(462),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1146 */   ERR_NOPERMFORHOST(463),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1153 */   ERR_PASSWDMISMATCH(464),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1160 */   ERR_YOUREBANNEDCREEP(465),
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1165 */   ERR_YOUWILLBEBANNED(466),
/*      */ 
/*      */ 
/*      */   
/* 1169 */   ERR_KEYSET(467),
/*      */ 
/*      */ 
/*      */   
/* 1173 */   ERR_CHANNELISFULL(471),
/*      */ 
/*      */ 
/*      */   
/* 1177 */   ERR_UNKNOWNMODE(472),
/*      */ 
/*      */ 
/*      */   
/* 1181 */   ERR_INVITEONLYCHAN(473),
/*      */ 
/*      */ 
/*      */   
/* 1185 */   ERR_BANNEDFROMCHAN(474),
/*      */ 
/*      */ 
/*      */   
/* 1189 */   ERR_BADCHANNELKEY(475),
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1194 */   ERR_BADCHANMASK(476),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1201 */   ERR_NOPRIVILEGES(481),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1209 */   ERR_CHANOPRIVSNEEDED(482),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1216 */   ERR_CANTKILLSERVER(483),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1224 */   ERR_NOOPERHOST(491),
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1229 */   ERR_NOSERVICEHOST(492),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1236 */   ERR_UMODEUNKNOWNFLAG(501),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1242 */   ERR_USERSDONTMATCH(502);
/*      */   private static final IRCReply[] CODE_LOOKUP;
/*      */   private static final int LOWEST_CODE;
/*      */   private final int code;
/*      */   
/*      */   static {
/* 1248 */     IRCReply[] vals = values();
/* 1249 */     int lowestCode = Integer.MAX_VALUE;
/* 1250 */     int highestCode = Integer.MIN_VALUE;
/* 1251 */     for (IRCReply rpl : vals) {
/* 1252 */       if (rpl.getCode() < lowestCode) {
/* 1253 */         lowestCode = rpl.getCode();
/*      */       }
/* 1255 */       if (rpl.getCode() > highestCode) {
/* 1256 */         highestCode = rpl.getCode();
/*      */       }
/*      */     } 
/* 1259 */     LOWEST_CODE = lowestCode;
/* 1260 */     CODE_LOOKUP = new IRCReply[highestCode - LOWEST_CODE + 1];
/* 1261 */     for (IRCReply rpl : vals)
/* 1262 */       CODE_LOOKUP[getLookUpIndex(rpl.getCode())] = rpl; 
/*      */   }
/*      */   
/*      */   public static IRCReply valueByCode(int code) {
/* 1266 */     return CODE_LOOKUP[getLookUpIndex(code)];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int getLookUpIndex(int c) {
/* 1274 */     return c - LOWEST_CODE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   IRCReply(int code) {
/* 1284 */     this.code = code;
/*      */   }
/*      */   
/*      */   public int getCode() {
/* 1288 */     return this.code;
/*      */   }
/*      */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\schwering\irc\li\\util\IRCReply.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */