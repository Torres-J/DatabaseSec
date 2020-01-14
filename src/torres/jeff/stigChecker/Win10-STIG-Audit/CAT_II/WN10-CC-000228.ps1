## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-74415"
$GroupTitle = "WN10-CC-000228"
$RuleID = "SV-89089r3_rule"
$Severity = "CAT II"
$RuleVersionSTIGID = "WN10-CC-000228"
$RuleTitle = "Windows 10 must be configured to prevent Microsoft Edge browser data from being cleared on exit."
$CCI = "CCI-000366"

$Configuration = ""
$Regkey = (Get-ItemProperty Registry::"HKEY_LOCAL_MACHINE\SOFTWARE\Policies\Microsoft\MicrosoftEdge\Privacy\").ClearBrowsingHistoryOnExit
if ($regkey -eq '0'){
    $Configuration = 'Completed'}
    else{
    $Configuration = 'Ongoing'}


$Audit = New-Object -TypeName System.Object
$Audit | Add-Member -MemberType NoteProperty -Name GroupID -Value $GroupID
$Audit | Add-Member -MemberType NoteProperty -Name Hostname -Value $Hostname
$Audit | Add-Member -MemberType NoteProperty -Name Configuration -Value $Configuration
$Audit | Add-Member -MemberType NoteProperty -Name Title -Value $Title
$Audit