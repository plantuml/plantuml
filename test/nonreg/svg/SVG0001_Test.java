package nonreg.svg;

import org.junit.jupiter.api.Test;

import java.io.IOException;

/*
Test diagram MUST be put between triple quotes

"""
@startuml
!pragma svginteractive false
actor my_actor
boundary my_boundary
collections my_collections
control my_control
database my_database
entity my_entity
participant my_participant
queue my_queue
my_actor -> my_database: Do something
my_database --> my_actor: Acknowledge it
@enduml
"""

Expected result MUST be put between triple brackets

{{{
<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" contentStyleType="text/css" data-diagram-type="SEQUENCE" height="241px" preserveAspectRatio="none" style="width:816px;height:241px;background:#FFFFFF;" version="1.1" viewBox="0 0 816 241" width="816px" zoomAndPan="magnify">
  <defs/>
  <g>
    <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5.0,5.0;" x1="38" x2="38" y1="81.4883" y2="160.1094"/>
    <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5.0,5.0;" x1="130.8516" x2="130.8516" y1="81.4883" y2="160.1094"/>
    <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5.0,5.0;" x1="248.6104" x2="248.6104" y1="81.4883" y2="160.1094"/>
    <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5.0,5.0;" x1="358.5381" x2="358.5381" y1="81.4883" y2="160.1094"/>
    <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5.0,5.0;" x1="456.9932" x2="456.9932" y1="81.4883" y2="160.1094"/>
    <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5.0,5.0;" x1="549.3682" x2="549.3682" y1="81.4883" y2="160.1094"/>
    <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5.0,5.0;" x1="653.0752" x2="653.0752" y1="81.4883" y2="160.1094"/>
    <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5.0,5.0;" x1="764.7002" x2="764.7002" y1="81.4883" y2="160.1094"/>
    <g>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing" textLength="61.8516" x="5" y="78.5352">my_actor</text>
      <ellipse cx="38.9258" cy="13.5" fill="#E2E2F0" rx="8" ry="8" style="stroke:#181818;stroke-width:0.5;"/>
      <path d="M38.9258,21.5 L38.9258,48.5 M25.9258,29.5 L51.9258,29.5 M38.9258,48.5 L25.9258,63.5 M38.9258,48.5 L51.9258,63.5 " fill="none" style="stroke:#181818;stroke-width:0.5;"/>
    </g>
    <g>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing" textLength="61.8516" x="5" y="172.6445">my_actor</text>
      <ellipse cx="38.9258" cy="184.0977" fill="#E2E2F0" rx="8" ry="8" style="stroke:#181818;stroke-width:0.5;"/>
      <path d="M38.9258,192.0977 L38.9258,219.0977 M25.9258,200.0977 L51.9258,200.0977 M38.9258,219.0977 L25.9258,234.0977 M38.9258,219.0977 L51.9258,234.0977 " fill="none" style="stroke:#181818;stroke-width:0.5;"/>
    </g>
    <g>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing" textLength="91.7588" x="82.8516" y="78.5352">my_boundary</text>
      <path d="M111.231,37 L111.231,61 M111.231,49 L128.231,49 " fill="none" style="stroke:#181818;stroke-width:0.5;"/>
      <ellipse cx="140.231" cy="49" fill="#E2E2F0" rx="12" ry="12" style="stroke:#181818;stroke-width:0.5;"/>
    </g>
    <g>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing" textLength="91.7588" x="82.8516" y="172.6445">my_boundary</text>
      <path d="M111.231,179.5977 L111.231,203.5977 M111.231,191.5977 L128.231,191.5977 " fill="none" style="stroke:#181818;stroke-width:0.5;"/>
      <ellipse cx="140.231" cy="191.5977" fill="#E2E2F0" rx="12" ry="12" style="stroke:#181818;stroke-width:0.5;"/>
    </g>
    <g>
      <rect fill="#E2E2F0" height="30.4883" style="stroke:#181818;stroke-width:0.5;" width="113.9277" x="194.6104" y="46"/>
      <rect fill="#E2E2F0" height="30.4883" style="stroke:#181818;stroke-width:0.5;" width="113.9277" x="190.6104" y="50"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing" textLength="99.9277" x="197.6104" y="70.5352">my_collections</text>
    </g>
    <g>
      <rect fill="#E2E2F0" height="30.4883" style="stroke:#181818;stroke-width:0.5;" width="113.9277" x="194.6104" y="159.1094"/>
      <rect fill="#E2E2F0" height="30.4883" style="stroke:#181818;stroke-width:0.5;" width="113.9277" x="190.6104" y="163.1094"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing" textLength="99.9277" x="197.6104" y="183.6445">my_collections</text>
    </g>
    <g>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing" textLength="75.4551" x="318.5381" y="78.5352">my_control</text>
      <ellipse cx="359.2656" cy="49" fill="#E2E2F0" rx="12" ry="12" style="stroke:#181818;stroke-width:0.5;"/>
      <polygon fill="#181818" points="355.2656,37,361.2656,32,359.2656,37,361.2656,42,355.2656,37" style="stroke:#181818;stroke-width:1;"/>
    </g>
    <g>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing" textLength="75.4551" x="318.5381" y="172.6445">my_control</text>
      <ellipse cx="359.2656" cy="191.5977" fill="#E2E2F0" rx="12" ry="12" style="stroke:#181818;stroke-width:0.5;"/>
      <polygon fill="#181818" points="355.2656,179.5977,361.2656,174.5977,359.2656,179.5977,361.2656,184.5977,355.2656,179.5977" style="stroke:#181818;stroke-width:1;"/>
    </g>
    <g>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing" textLength="88.375" x="409.9932" y="78.5352">my_database</text>
      <path d="M439.1807,29 C439.1807,19 457.1807,19 457.1807,19 C457.1807,19 475.1807,19 475.1807,29 L475.1807,55 C475.1807,65 457.1807,65 457.1807,65 C457.1807,65 439.1807,65 439.1807,55 L439.1807,29 " fill="#E2E2F0" style="stroke:#181818;stroke-width:1.5;"/>
      <path d="M439.1807,29 C439.1807,39 457.1807,39 457.1807,39 C457.1807,39 475.1807,39 475.1807,29 " fill="none" style="stroke:#181818;stroke-width:1.5;"/>
    </g>
    <g>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing" textLength="88.375" x="409.9932" y="172.6445">my_database</text>
      <path d="M439.1807,185.5977 C439.1807,175.5977 457.1807,175.5977 457.1807,175.5977 C457.1807,175.5977 475.1807,175.5977 475.1807,185.5977 L475.1807,211.5977 C475.1807,221.5977 457.1807,221.5977 457.1807,221.5977 C457.1807,221.5977 439.1807,221.5977 439.1807,211.5977 L439.1807,185.5977 " fill="#E2E2F0" style="stroke:#181818;stroke-width:1.5;"/>
      <path d="M439.1807,185.5977 C439.1807,195.5977 457.1807,195.5977 457.1807,195.5977 C457.1807,195.5977 475.1807,195.5977 475.1807,185.5977 " fill="none" style="stroke:#181818;stroke-width:1.5;"/>
    </g>
    <g>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing" textLength="65.707" x="514.3682" y="78.5352">my_entity</text>
      <ellipse cx="550.2217" cy="49" fill="#E2E2F0" rx="12" ry="12" style="stroke:#181818;stroke-width:0.5;"/>
      <line style="stroke:#181818;stroke-width:0.5;" x1="538.2217" x2="562.2217" y1="63" y2="63"/>
    </g>
    <g>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing" textLength="65.707" x="514.3682" y="172.6445">my_entity</text>
      <ellipse cx="550.2217" cy="191.5977" fill="#E2E2F0" rx="12" ry="12" style="stroke:#181818;stroke-width:0.5;"/>
      <line style="stroke:#181818;stroke-width:0.5;" x1="538.2217" x2="562.2217" y1="205.5977" y2="205.5977"/>
    </g>
    <g>
      <rect fill="#E2E2F0" height="30.4883" rx="2.5" ry="2.5" style="stroke:#181818;stroke-width:0.5;" width="114.625" x="596.0752" y="50"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing" textLength="100.625" x="603.0752" y="70.5352">my_participant</text>
    </g>
    <g>
      <rect fill="#E2E2F0" height="30.4883" rx="2.5" ry="2.5" style="stroke:#181818;stroke-width:0.5;" width="114.625" x="596.0752" y="159.1094"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing" textLength="100.625" x="603.0752" y="179.6445">my_participant</text>
    </g>
    <g>
      <path d="M725.7002,55 L804.873,55 C809.873,55 809.873,68.2441 809.873,68.2441 C809.873,68.2441 809.873,81.4883 804.873,81.4883 L725.7002,81.4883 C720.7002,81.4883 720.7002,68.2441 720.7002,68.2441 C720.7002,68.2441 720.7002,55 725.7002,55 " fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <path d="M804.873,55 C799.873,55 799.873,68.2441 799.873,68.2441 C799.873,81.4883 804.873,81.4883 804.873,81.4883 " fill="none" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing" textLength="69.1729" x="725.7002" y="73.5352">my_queue</text>
    </g>
    <g>
      <path d="M725.7002,159.1094 L804.873,159.1094 C809.873,159.1094 809.873,172.3535 809.873,172.3535 C809.873,172.3535 809.873,185.5977 804.873,185.5977 L725.7002,185.5977 C720.7002,185.5977 720.7002,172.3535 720.7002,172.3535 C720.7002,172.3535 720.7002,159.1094 725.7002,159.1094 " fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <path d="M804.873,159.1094 C799.873,159.1094 799.873,172.3535 799.873,172.3535 C799.873,185.5977 804.873,185.5977 804.873,185.5977 " fill="none" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing" textLength="69.1729" x="725.7002" y="177.6445">my_queue</text>
    </g>
    <g>
      <polygon fill="#181818" points="445.1807,108.7988,455.1807,112.7988,445.1807,116.7988,449.1807,112.7988" style="stroke:#181818;stroke-width:1;"/>
      <line style="stroke:#181818;stroke-width:1;" x1="38.9258" x2="451.1807" y1="112.7988" y2="112.7988"/>
      <text fill="#000000" font-family="sans-serif" font-size="13" lengthAdjust="spacing" textLength="88.6895" x="45.9258" y="108.0566">Do something</text>
    </g>
    <g>
      <polygon fill="#181818" points="49.9258,138.1094,39.9258,142.1094,49.9258,146.1094,45.9258,142.1094" style="stroke:#181818;stroke-width:1;"/>
      <line style="stroke:#181818;stroke-width:1;stroke-dasharray:2.0,2.0;" x1="43.9258" x2="456.1807" y1="142.1094" y2="142.1094"/>
      <text fill="#000000" font-family="sans-serif" font-size="13" lengthAdjust="spacing" textLength="96.5605" x="55.9258" y="137.3672">Acknowledge it</text>
    </g>
  </g>
</svg>
}}}

 */
public class SVG0001_Test extends SvgTest {

	@Test
	void testSequenceDiagramHasGroupedParticipantsWithClasses() throws IOException {
		checkXmlAndDescription("(8 participants)", true);
	}

}
