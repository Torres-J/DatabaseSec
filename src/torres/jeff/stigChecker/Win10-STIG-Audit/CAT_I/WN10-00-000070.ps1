## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-63361"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Windows 10 Security Technical Implementation Guide"


if ($Hostname -eq $null){

$Hostname = $env:computername}

$Configuration = ""

$Adminlist = @((
"AR\USAR_DeskMgt
AR\USAR_ScanAdmins
AR\USARC DeskMgt
AR\USARC OUAdmin
$env:computername\DoD_Admin
$env:computername\xAdministrator") | Out-String)
$LocalAdmin = @((Get-LocalGroupMember Administrators).name | Out-String)
$Check = $Localadmin | Where {$Adminlist -notcontains $_}
if ($Check -eq $null){$Configuration = "Completed"}else{$Configuration = "Ongoing"}

$Audit = New-Object -TypeName System.Object
$Audit | Add-Member -MemberType NoteProperty -Name GroupID -Value $GroupID
$Audit | Add-Member -MemberType NoteProperty -Name Hostname -Value $Hostname
$Audit | Add-Member -MemberType NoteProperty -Name Configuration -Value $Configuration
$Audit | Add-Member -MemberType NoteProperty -Name Title -Value $Title
$Audit